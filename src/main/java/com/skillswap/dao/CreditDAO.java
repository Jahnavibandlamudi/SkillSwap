package com.skillswap.dao;

import com.skillswap.model.CreditAccount;
import com.skillswap.model.CreditTransaction;
import com.skillswap.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Credit Wallets and Credit Transactions.
 * Guarantees ACID atomic credit transfers.
 */
public class CreditDAO {

    private static final Logger LOGGER = Logger.getLogger(CreditDAO.class.getName());

    public CreditAccount getAccount(int userId) {
        String sql = "SELECT * FROM credit_accounts WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new CreditAccount(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getInt("balance"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching credit account for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public int getBalance(int userId) {
        CreditAccount acc = getAccount(userId);
        return acc != null ? acc.getBalance() : 0;
    }

    public boolean createInitialAccount(int userId, int initialCredits) {
        Connection conn = null;
        PreparedStatement psAccount = null;
        PreparedStatement psTx = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String accSql = "INSERT INTO credit_accounts (user_id, balance) VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE balance = balance";
            psAccount = conn.prepareStatement(accSql);
            psAccount.setInt(1, userId);
            psAccount.setInt(2, initialCredits);
            psAccount.executeUpdate();

            String txSql = "INSERT INTO credit_transactions (user_id, session_id, amount, transaction_type, description) " +
                           "VALUES (?, NULL, ?, 'WELCOME', 'Welcome bonus credits for joining SkillSwap SRM AP')";
            psTx = conn.prepareStatement(txSql);
            psTx.setInt(1, userId);
            psTx.setInt(2, initialCredits);
            psTx.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { LOGGER.log(Level.SEVERE, "Rollback failed", ex); }
            }
            LOGGER.log(Level.SEVERE, "Error initializing credit account for user: " + userId, e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
            DBConnection.closeQuietly(psAccount, psTx, conn);
        }
        return false;
    }

    public List<CreditTransaction> getTransactions(int userId) {
        List<CreditTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM credit_transactions WHERE user_id = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                CreditTransaction tx = new CreditTransaction();
                tx.setTransactionId(rs.getInt("transaction_id"));
                tx.setUserId(rs.getInt("user_id"));
                int sessId = rs.getInt("session_id");
                tx.setSessionId(rs.wasNull() ? null : sessId);
                tx.setAmount(rs.getInt("amount"));
                tx.setTransactionType(rs.getString("transaction_type"));
                tx.setDescription(rs.getString("description"));
                tx.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(tx);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching credit transactions for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public boolean hasSessionTransaction(int sessionId) {
        String sql = "SELECT COUNT(*) FROM credit_transactions WHERE session_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sessionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking session credit transaction: " + sessionId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return false;
    }

    /**
     * Atomically transfers credits upon two-way confirmed session completion.
     * Checks learner balance, prevents duplicates, inserts ledger entries, and commits or rolls back.
     *
     * @param teacherId Teacher user ID (earns +credits)
     * @param learnerId Learner user ID (spends -credits)
     * @param sessionId Completed session ID
     * @param amount Credit amount (standard: 2)
     * @param skillName Name of skill taught
     * @param teacherName Full name of teacher
     * @param learnerName Full name of learner
     * @return true if atomic transfer succeeded, false otherwise
     */
    public boolean executeSessionCreditTransfer(int teacherId, int learnerId, int sessionId, int amount,
                                               String skillName, String teacherName, String learnerName) {
        Connection conn = null;
        PreparedStatement psCheck = null;
        PreparedStatement psLearnerCheck = null;
        PreparedStatement psDeduct = null;
        PreparedStatement psAdd = null;
        PreparedStatement psTxLearner = null;
        PreparedStatement psTxTeacher = null;
        PreparedStatement psNotif1 = null;
        PreparedStatement psNotif2 = null;
        ResultSet rsCheck = null;
        ResultSet rsLearner = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Prevent duplicate credit transfer
            String checkSql = "SELECT COUNT(*) FROM credit_transactions WHERE session_id = ?";
            psCheck = conn.prepareStatement(checkSql);
            psCheck.setInt(1, sessionId);
            rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                LOGGER.warning("Duplicate credit transfer blocked for session: " + sessionId);
                conn.rollback();
                return false;
            }

            // 2. Lock & Check learner balance
            String lockSql = "SELECT balance FROM credit_accounts WHERE user_id = ? FOR UPDATE";
            psLearnerCheck = conn.prepareStatement(lockSql);
            psLearnerCheck.setInt(1, learnerId);
            rsLearner = psLearnerCheck.executeQuery();
            if (!rsLearner.next()) {
                LOGGER.warning("Credit account not found for learner: " + learnerId);
                conn.rollback();
                return false;
            }
            int currentLearnerBalance = rsLearner.getInt("balance");
            if (currentLearnerBalance < amount) {
                LOGGER.warning("Insufficient credits for learner " + learnerId + ". Balance: " + currentLearnerBalance);
                conn.rollback();
                return false;
            }

            // 3. Deduct from learner
            String deductSql = "UPDATE credit_accounts SET balance = balance - ? WHERE user_id = ?";
            psDeduct = conn.prepareStatement(deductSql);
            psDeduct.setInt(1, amount);
            psDeduct.setInt(2, learnerId);
            psDeduct.executeUpdate();

            // 4. Add to teacher
            String addSql = "UPDATE credit_accounts SET balance = balance + ? WHERE user_id = ?";
            psAdd = conn.prepareStatement(addSql);
            psAdd.setInt(1, amount);
            psAdd.setInt(2, teacherId);
            psAdd.executeUpdate();

            // 5. Insert learner SPEND transaction record
            String txSql = "INSERT INTO credit_transactions (user_id, session_id, amount, transaction_type, description) " +
                           "VALUES (?, ?, ?, ?, ?)";
            psTxLearner = conn.prepareStatement(txSql);
            psTxLearner.setInt(1, learnerId);
            psTxLearner.setInt(2, sessionId);
            psTxLearner.setInt(3, -amount);
            psTxLearner.setString(4, "SPEND");
            psTxLearner.setString(5, "Learned " + skillName + " from " + teacherName);
            psTxLearner.executeUpdate();

            // 6. Insert teacher EARN transaction record
            psTxTeacher = conn.prepareStatement(txSql);
            psTxTeacher.setInt(1, teacherId);
            psTxTeacher.setInt(2, sessionId);
            psTxTeacher.setInt(3, amount);
            psTxTeacher.setString(4, "EARN");
            psTxTeacher.setString(5, "Taught " + skillName + " to " + learnerName);
            psTxTeacher.executeUpdate();

            // 7. Insert Notifications
            String notifSql = "INSERT INTO notifications (user_id, message, type, link) VALUES (?, ?, ?, ?)";
            psNotif1 = conn.prepareStatement(notifSql);
            psNotif1.setInt(1, teacherId);
            psNotif1.setString(2, "Session on " + skillName + " completed! +" + amount + " credits added to your wallet.");
            psNotif1.setString(3, "WALLET");
            psNotif1.setString(4, "credits");
            psNotif1.executeUpdate();

            psNotif2 = conn.prepareStatement(notifSql);
            psNotif2.setInt(1, learnerId);
            psNotif2.setString(2, "Session on " + skillName + " completed! " + amount + " credits deducted from your wallet.");
            psNotif2.setString(3, "WALLET");
            psNotif2.setString(4, "credits");
            psNotif2.executeUpdate();

            // Commit atomic transaction
            conn.commit();
            LOGGER.info("Atomic credit transfer completed for session " + sessionId + ": Teacher " + teacherId +
                        " earned " + amount + ", Learner " + learnerId + " spent " + amount);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.info("Rolled back credit transaction for session " + sessionId);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback error", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Error executing atomic session credit transfer for session: " + sessionId, e);
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
            DBConnection.closeQuietly(rsCheck, psCheck, null);
            DBConnection.closeQuietly(rsLearner, psLearnerCheck, null);
            DBConnection.closeQuietly(null, psDeduct, null);
            DBConnection.closeQuietly(null, psAdd, null);
            DBConnection.closeQuietly(null, psTxLearner, null);
            DBConnection.closeQuietly(null, psTxTeacher, null);
            DBConnection.closeQuietly(null, psNotif1, null);
            DBConnection.closeQuietly(null, psNotif2, conn);
        }
    }
}

