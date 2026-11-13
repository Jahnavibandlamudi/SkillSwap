<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Virtual Credit Wallet" />
<c:set var="activePage" value="credits" />
<jsp:include page="/WEB-INF/partials/header.jsp" />
<jsp:include page="/WEB-INF/partials/navbar.jsp" />

<main class="py-4 bg-page">
    <div class="container">

        <!-- Header -->
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
            <div>
                <h3 class="fw-bold text-dark mb-1">
                    <i class="bi bi-wallet2 text-primary me-1"></i> Virtual Credit Wallet
                </h3>
                <p class="text-muted small mb-0">Internal non-monetary currency powering peer-to-peer learning accountability across SRM University AP.</p>
            </div>
            <div>
                <span class="badge bg-light text-secondary border px-3 py-2 small">
                    <i class="bi bi-shield-check text-success me-1"></i> ACID Atomic Transactions
                </span>
            </div>
        </div>

        <div class="row g-4 mb-4">
            <!-- Wallet Balance Card -->
            <div class="col-lg-6">
                <div class="wallet-card h-100 d-flex flex-column justify-content-between">
                    <div>
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <div>
                                <span class="badge bg-white text-dark fw-bold px-2 py-1 rounded-pill small mb-2">
                                    SRM AP Student Wallet
                                </span>
                                <div class="text-uppercase small tracking-wide opacity-75">Available Credit Balance</div>
                                <div class="wallet-balance mt-1">${balance} <span class="fs-4 fw-normal opacity-75">Credits</span></div>
                            </div>
                            <div class="avatar avatar-md bg-white bg-opacity-25 text-white">
                                <i class="bi bi-coin fs-4"></i>
                            </div>
                        </div>
                    </div>

                    <div class="pt-3 border-top border-white border-opacity-25 d-flex justify-content-between align-items-center small">
                        <div>
                            <div class="opacity-75">Account Holder</div>
                            <div class="fw-bold">${sessionScope.user.fullName} (${sessionScope.user.studentId})</div>
                        </div>
                        <div class="text-end">
                            <div class="opacity-75">Status</div>
                            <span class="badge bg-success-subtle text-success">Active &bull; Verified</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Economy Rules & Stats -->
            <div class="col-lg-6">
                <div class="row g-3 h-100">
                    <div class="col-sm-6">
                        <div class="stat-card h-100">
                            <div class="stat-icon emerald"><i class="bi bi-graph-up-arrow"></i></div>
                            <div class="stat-value text-success">+${totalEarned}</div>
                            <div class="stat-label">Total Credits Earned</div>
                            <div class="small text-muted mt-2" style="font-size: 0.75rem;">From teaching peer sessions and welcome bonus.</div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="stat-card h-100">
                            <div class="stat-icon indigo"><i class="bi bi-graph-down-arrow"></i></div>
                            <div class="stat-value text-danger">-${totalSpent}</div>
                            <div class="stat-label">Total Credits Spent</div>
                            <div class="small text-muted mt-2" style="font-size: 0.75rem;">Invested in learning sessions with fellow students.</div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="card-custom p-3 bg-white">
                            <div class="fw-bold small text-dark mb-2"><i class="bi bi-info-circle text-primary me-1"></i> How Virtual Credits Work</div>
                            <ul class="small text-muted mb-0 ps-3">
                                <li><strong>New Registration:</strong> 5 welcome credits granted immediately.</li>
                                <li><strong>Teaching a Session:</strong> Teacher receives <strong>+2 credits</strong>.</li>
                                <li><strong>Attending a Session:</strong> Learner spends <strong>-2 credits</strong>.</li>
                                <li>Credits transfer only after <em>both</em> participants confirm session completion.</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Ledger Transaction History Table -->
        <div class="card-custom bg-white">
            <div class="card-header-clean">
                <div class="d-flex align-items-center gap-2">
                    <i class="bi bi-receipt text-primary fs-5"></i>
                    <span class="fw-bold">Immutable Transaction Ledger</span>
                </div>
                <span class="badge bg-primary-subtle text-primary">${transactions.size()} Records</span>
            </div>
            <div class="p-3">
                <c:choose>
                    <c:when test="${not empty transactions}">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle small mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>Tx ID</th>
                                        <th>Timestamp</th>
                                        <th>Transaction Type</th>
                                        <th>Description</th>
                                        <th class="text-end">Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="tx" items="${transactions}">
                                        <tr>
                                            <td class="text-muted">#${tx.transactionId}</td>
                                            <td>${tx.createdAt}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${tx.transactionType == 'WELCOME'}">
                                                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle">WELCOME BONUS</span>
                                                    </c:when>
                                                    <c:when test="${tx.transactionType == 'EARN'}">
                                                        <span class="badge bg-success-subtle text-success border border-success-subtle">EARNED</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle">SPENT</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="fw-semibold text-dark">${tx.description}</td>
                                            <td class="text-end fw-bold ${tx.amount > 0 ? 'text-success' : 'text-danger'}">
                                                ${tx.amount > 0 ? '+' : ''}${tx.amount} Credits
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5 text-muted small">
                            <i class="bi bi-wallet text-secondary fs-2 d-block mb-2"></i>
                            No transaction records found yet.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

