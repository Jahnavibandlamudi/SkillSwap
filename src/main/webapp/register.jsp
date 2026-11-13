<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Student Registration" />
<jsp:include page="/WEB-INF/partials/header.jsp" />
<jsp:include page="/WEB-INF/partials/navbar.jsp" />

<main class="py-5 bg-page">
    <div class="container py-3">
        <div class="row justify-content-center">
            <div class="col-lg-7 col-md-10">

                <div class="card-custom p-4 p-md-5 border-0 shadow-lg bg-white">
                    <div class="d-flex align-items-center justify-content-between mb-4 border-bottom pb-3">
                        <div>
                            <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-1 mb-2">
                                <i class="bi bi-gift-fill me-1"></i> Includes 5 Free Welcome Credits
                            </span>
                            <h3 class="fw-bold text-dark mb-0">Create Student Account</h3>
                            <p class="text-muted small mb-0">Exclusive to SRM University AP students</p>
                        </div>
                        <div class="avatar avatar-lg bg-primary-subtle text-primary d-none d-sm-flex">
                            <i class="bi bi-mortarboard-fill"></i>
                        </div>
                    </div>

                    <!-- Alerts -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show small d-flex align-items-center gap-2" role="alert">
                            <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                            <div>${errorMessage}</div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="POST">
                        <div class="row g-3">
                            <!-- Student ID -->
                            <div class="col-md-6">
                                <label class="form-label" for="studentId">SRM AP Student ID <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="studentId" name="studentId"
                                       value="${studentId != null ? studentId : ''}"
                                       placeholder="e.g. AP21110010001" required>
                                <div id="studentIdFeedback" class="form-text small text-muted">Format: AP followed by 11 digits (e.g. AP21110010001).</div>
                            </div>

                            <!-- Full Name -->
                            <div class="col-md-6">
                                <label class="form-label" for="fullName">Full Name <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="fullName" name="fullName"
                                       value="${fullName != null ? fullName : ''}"
                                       placeholder="e.g. Aarav Sharma" required>
                            </div>

                            <!-- College Email -->
                            <div class="col-md-12">
                                <label class="form-label" for="email">SRM AP College Email <span class="text-danger">*</span></label>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="${email != null ? email : ''}"
                                       placeholder="e.g. aarav_sharma@srmap.edu.in" required>
                                <div class="form-text small text-muted">Use your college-issued @srmap.edu.in email address.</div>
                            </div>

                            <!-- College Name (Locked) -->
                            <div class="col-md-12">
                                <label class="form-label" for="college">College / University</label>
                                <input type="text" class="form-control bg-light" id="college" name="college"
                                       value="SRM UNIVERSITY AP" readonly>
                            </div>

                            <!-- Department -->
                            <div class="col-md-6">
                                <label class="form-label" for="department">Department / Branch <span class="text-danger">*</span></label>
                                <select class="form-select" id="department" name="department" required>
                                    <option value="">Choose Department...</option>
                                    <option value="Computer Science and Engineering" ${department == 'Computer Science and Engineering' ? 'selected' : ''}>Computer Science and Engineering (CSE)</option>
                                    <option value="Electronics and Communication Engineering" ${department == 'Electronics and Communication Engineering' ? 'selected' : ''}>Electronics and Communication Engineering (ECE)</option>
                                    <option value="Artificial Intelligence and Data Science" ${department == 'Artificial Intelligence and Data Science' ? 'selected' : ''}>Artificial Intelligence &amp; Data Science (AI &amp; DS)</option>
                                    <option value="Electrical and Electronics Engineering" ${department == 'Electrical and Electronics Engineering' ? 'selected' : ''}>Electrical and Electronics Engineering (EEE)</option>
                                    <option value="Mechanical Engineering" ${department == 'Mechanical Engineering' ? 'selected' : ''}>Mechanical Engineering (MECH)</option>
                                    <option value="Civil Engineering" ${department == 'Civil Engineering' ? 'selected' : ''}>Civil Engineering (CIVIL)</option>
                                    <option value="School of Entrepreneurship and Management Studies" ${department == 'School of Entrepreneurship and Management Studies' ? 'selected' : ''}>School of Entrepreneurship and Management Studies (SEAMS)</option>
                                    <option value="Liberal Arts and Basic Sciences" ${department == 'Liberal Arts and Basic Sciences' ? 'selected' : ''}>Liberal Arts &amp; Basic Sciences (SLABS)</option>
                                </select>
                            </div>

                            <!-- Year of Study -->
                            <div class="col-md-6">
                                <label class="form-label" for="yearOfStudy">Year of Study <span class="text-danger">*</span></label>
                                <select class="form-select" id="yearOfStudy" name="yearOfStudy" required>
                                    <option value="1" ${yearOfStudy == 1 ? 'selected' : ''}>1st Year (Freshman)</option>
                                    <option value="2" ${yearOfStudy == 2 ? 'selected' : ''}>2nd Year (Sophomore)</option>
                                    <option value="3" ${yearOfStudy == 3 ? 'selected' : ''}>3rd Year (Junior)</option>
                                    <option value="4" ${yearOfStudy == 4 ? 'selected' : ''}>4th Year (Senior)</option>
                                    <option value="5" ${yearOfStudy == 5 ? 'selected' : ''}>5th Year (Dual Degree / PG)</option>
                                </select>
                            </div>

                            <!-- Password -->
                            <div class="col-md-6">
                                <label class="form-label" for="password">Password <span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="Minimum 6 characters" required minlength="6">
                            </div>

                            <!-- Confirm Password -->
                            <div class="col-md-6">
                                <label class="form-label" for="confirmPassword">Confirm Password <span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                                       placeholder="Re-type password" required minlength="6">
                            </div>

                            <!-- Bio -->
                            <div class="col-md-12">
                                <label class="form-label" for="bio">Short Bio &amp; Learning Goals</label>
                                <textarea class="form-control" id="bio" name="bio" rows="3"
                                          placeholder="Tell peers what you love to build, clubs you belong to at SRM AP, and what skills you are eager to pick up.">${bio != null ? bio : ''}</textarea>
                                <div class="form-text small text-muted">A clear bio increases your match compatibility score by 10%.</div>
                            </div>
                        </div>

                        <div class="d-grid mt-4">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-check2-circle me-1"></i> Register Student Account &amp; Claim 5 Credits
                            </button>
                        </div>
                    </form>

                    <div class="text-center mt-4 pt-3 border-top">
                        <span class="text-muted small">Already registered at SRM AP?</span>
                        <a href="${pageContext.request.contextPath}/login" class="fw-bold text-primary text-decoration-none small ms-1">
                            Sign In to your account &rarr;
                        </a>
                    </div>
                </div>

            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

