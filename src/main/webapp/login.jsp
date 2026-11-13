<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign In" />
<jsp:include page="/WEB-INF/partials/header.jsp" />
<jsp:include page="/WEB-INF/partials/navbar.jsp" />

<main class="py-5 bg-page">
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-8">
                
                <div class="card-custom p-4 p-md-5 border-0 shadow-lg bg-white">
                    <div class="text-center mb-4">
                        <div class="d-inline-flex p-3 rounded-circle bg-primary-subtle text-primary mb-3">
                            <i class="bi bi-mortarboard fs-2"></i>
                        </div>
                        <h3 class="fw-bold text-dark mb-1">Student & Admin Sign In</h3>
                        <p class="text-muted small">Access your SRM University AP SkillSwap Portal</p>
                    </div>

                    <!-- Alerts -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show small d-flex align-items-center gap-2" role="alert">
                            <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                            <div>${errorMessage}</div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${param.message == 'logged_out'}">
                        <div class="alert alert-success alert-dismissible fade show small d-flex align-items-center gap-2" role="alert">
                            <i class="bi bi-check-circle-fill fs-5"></i>
                            <div>You have been safely signed out.</div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="POST">
                        <div class="mb-3">
                            <label class="form-label" for="identifier">SRM AP Email or Student ID</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0"><i class="bi bi-person"></i></span>
                                <input type="text" class="form-control border-start-0" id="identifier" name="identifier"
                                       value="${identifier != null ? identifier : ''}"
                                       placeholder="e.g. aarav_sharma@srmap.edu.in or AP21110010001" required autofocus>
                            </div>
                            <div class="form-text small">Accepted format: AP*********** or your @srmap.edu.in email.</div>
                        </div>

                        <div class="mb-3">
                            <div class="d-flex justify-content-between">
                                <label class="form-label" for="password">Password</label>
                            </div>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0"><i class="bi bi-lock"></i></span>
                                <input type="password" class="form-control border-start-0" id="password" name="password"
                                       placeholder="Enter your account password" required>
                            </div>
                        </div>

                        <div class="d-grid mb-3">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-box-arrow-in-right me-1"></i> Sign In to SkillSwap
                            </button>
                        </div>
                    </form>

                    <!-- One-Click Seed Credentials for Testing -->
                    <div class="mt-4 pt-3 border-top">
                        <div class="small fw-bold text-uppercase text-muted mb-2 text-center">
                            <i class="bi bi-lightning-charge-fill text-warning me-1"></i> Quick Test Accounts (1-Click Fill)
                        </div>
                        <div class="d-flex flex-column gap-2">
                            <button type="button" class="btn btn-sm btn-subtle text-start d-flex justify-content-between align-items-center"
                                    onclick="fillLogin('aarav_sharma@srmap.edu.in', 'Password@123')">
                                <span><strong>Aarav Sharma</strong> (Offers Java &bull; Wants Design)</span>
                                <span class="badge bg-primary text-white">Student</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-subtle text-start d-flex justify-content-between align-items-center"
                                    onclick="fillLogin('diya_reddy@srmap.edu.in', 'Password@123')">
                                <span><strong>Diya Reddy</strong> (Offers Design &bull; Wants Java)</span>
                                <span class="badge bg-primary text-white">Student</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-subtle text-start d-flex justify-content-between align-items-center"
                                    onclick="fillLogin('admin@srmap.edu.in', 'Admin@123')">
                                <span><strong>Administrator</strong> (Full Platform Control)</span>
                                <span class="badge bg-dark text-white">Admin</span>
                            </button>
                        </div>
                    </div>

                    <div class="text-center mt-4 pt-2">
                        <span class="text-muted small">New to SkillSwap SRM AP?</span>
                        <a href="${pageContext.request.contextPath}/register" class="fw-bold text-primary text-decoration-none small ms-1">
                            Register Student Account &rarr;
                        </a>
                    </div>
                </div>

            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

