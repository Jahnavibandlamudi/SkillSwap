<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Student Profile" />
<c:set var="activePage" value="profile" />
<jsp:include page="/WEB-INF/partials/header.jsp" />
<jsp:include page="/WEB-INF/partials/navbar.jsp" />

<main class="py-4 bg-page">
    <div class="container">

        <!-- Flash alerts -->
        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success alert-dismissible fade show small d-flex align-items-center gap-2 mb-4" role="alert">
                <i class="bi bi-check-circle-fill fs-5"></i>
                <div>${flashSuccess}</div>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show small d-flex align-items-center gap-2 mb-4" role="alert">
                <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                <div>${errorMessage}</div>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Profile Header Card -->
        <div class="card-custom p-4 mb-4 bg-white">
            <div class="row align-items-center g-4">
                <div class="col-auto">
                    <div class="avatar avatar-xl bg-primary text-white shadow-sm">
                        ${profileUser.fullName != null ? profileUser.fullName.substring(0, 1) : 'U'}
                    </div>
                </div>
                <div class="col">
                    <div class="d-flex flex-wrap align-items-center gap-2 mb-1">
                        <h3 class="fw-bold text-dark mb-0">${profileUser.fullName}</h3>
                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle">${profileUser.studentId}</span>
                        <span class="badge bg-light text-secondary border">Year ${profileUser.yearOfStudy}</span>
                        <c:if test="${isOwnProfile}">
                            <span class="badge bg-success-subtle text-success border border-success-subtle">Your Profile</span>
                        </c:if>
                    </div>
                    <div class="text-secondary small mb-2">
                        <i class="bi bi-building me-1"></i> ${profileUser.college} &bull; ${profileUser.department}
                    </div>
                    <p class="text-dark small mb-2" style="max-width: 800px;">
                        ${not empty profileUser.bio ? profileUser.bio : 'No bio provided yet.'}
                    </p>
                    
                    <!-- Rating & Completed Sessions Stats -->
                    <div class="d-flex align-items-center gap-3">
                        <div class="d-flex align-items-center gap-1 small">
                            <span class="star-rating">
                                <i class="bi bi-star-fill"></i>
                            </span>
                            <strong class="text-dark">${profileUser.averageRating > 0 ? profileUser.averageRating : 'New'}</strong>
                            <span class="text-muted">(${reviews.size()} reviews)</span>
                        </div>
                        <div class="text-muted small">&bull;</div>
                        <div class="small text-muted">
                            <i class="bi bi-calendar-check me-1 text-primary"></i>
                            <strong>${completedSessions.size()}</strong> completed sessions
                        </div>
                        <c:if test="${isOwnProfile}">
                            <div class="text-muted small">&bull;</div>
                            <div class="small text-muted">
                                <i class="bi bi-coin me-1 text-warning"></i>
                                <strong>${profileUser.creditBalance}</strong> credits in wallet
                            </div>
                        </c:if>
                    </div>
                </div>

                <c:if test="${isOwnProfile}">
                    <div class="col-lg-auto d-flex flex-wrap gap-2">
                        <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                            <i class="bi bi-pencil me-1"></i> Edit Profile
                        </button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                            <i class="bi bi-key me-1"></i> Password
                        </button>
                    </div>
                </c:if>
                <c:if test="${!isOwnProfile}">
                    <div class="col-lg-auto">
                        <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary btn-sm">
                            <i class="bi bi-arrow-left-right me-1"></i> Propose Skill Exchange
                        </a>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="row g-4">
            <!-- Left Column: Skills & Availability Schedule -->
            <div class="col-lg-7">
                
                <!-- Skills Offered (Teaching) -->
                <div class="card-custom mb-4">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-award text-success fs-5"></i>
                            <span class="fw-bold">Skills ${isOwnProfile ? 'You Can Teach' : 'Offered by ' += profileUser.fullName}</span>
                        </div>
                        <c:if test="${isOwnProfile}">
                            <a href="${pageContext.request.contextPath}/skills" class="btn btn-sm btn-subtle">
                                <i class="bi bi-plus-circle me-1"></i> Add Skill
                            </a>
                        </c:if>
                    </div>
                    <div class="p-3">
                        <c:choose>
                            <c:when test="${not empty offeredSkills}">
                                <div class="d-flex flex-wrap gap-2">
                                    <c:forEach var="sk" items="${offeredSkills}">
                                        <div class="p-2 px-3 border rounded-3 bg-light d-flex align-items-center gap-2">
                                            <div>
                                                <div class="fw-bold small text-dark">${sk.skillName}</div>
                                                <div class="d-flex gap-1 align-items-center">
                                                    <span class="badge bg-secondary-subtle text-secondary" style="font-size: 0.68rem;">${sk.category}</span>
                                                    <span class="badge badge-proficiency">${sk.proficiency}</span>
                                                </div>
                                            </div>
                                            <c:if test="${isOwnProfile}">
                                                <form action="${pageContext.request.contextPath}/skills" method="POST" class="ms-2">
                                                    <input type="hidden" name="action" value="removeSkill">
                                                    <input type="hidden" name="userSkillId" value="${sk.userSkillId}">
                                                    <button type="submit" class="btn btn-sm text-danger p-0 border-0" title="Remove skill">
                                                        <i class="bi bi-x-circle"></i>
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small mb-0">No teaching skills listed yet.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Skills Wanted (Learning) -->
                <div class="card-custom mb-4">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-mortarboard text-primary fs-5"></i>
                            <span class="fw-bold">Skills ${isOwnProfile ? 'You Want to Learn' : 'Wanted by ' += profileUser.fullName}</span>
                        </div>
                        <c:if test="${isOwnProfile}">
                            <a href="${pageContext.request.contextPath}/skills" class="btn btn-sm btn-subtle">
                                <i class="bi bi-plus-circle me-1"></i> Add Interest
                            </a>
                        </c:if>
                    </div>
                    <div class="p-3">
                        <c:choose>
                            <c:when test="${not empty wantedSkills}">
                                <div class="d-flex flex-wrap gap-2">
                                    <c:forEach var="sk" items="${wantedSkills}">
                                        <div class="p-2 px-3 border rounded-3 bg-light d-flex align-items-center gap-2">
                                            <div>
                                                <div class="fw-bold small text-dark">${sk.skillName}</div>
                                                <div class="d-flex gap-1 align-items-center">
                                                    <span class="badge bg-secondary-subtle text-secondary" style="font-size: 0.68rem;">${sk.category}</span>
                                                    <span class="badge badge-proficiency">${sk.proficiency}</span>
                                                </div>
                                            </div>
                                            <c:if test="${isOwnProfile}">
                                                <form action="${pageContext.request.contextPath}/skills" method="POST" class="ms-2">
                                                    <input type="hidden" name="action" value="removeSkill">
                                                    <input type="hidden" name="userSkillId" value="${sk.userSkillId}">
                                                    <button type="submit" class="btn btn-sm text-danger p-0 border-0" title="Remove interest">
                                                        <i class="bi bi-x-circle"></i>
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small mb-0">No learning interests listed yet.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Schedule Availability -->
                <div class="card-custom">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-clock-history text-primary fs-5"></i>
                            <span class="fw-bold">Weekly Schedule Availability</span>
                        </div>
                        <c:if test="${isOwnProfile}">
                            <a href="${pageContext.request.contextPath}/skills#availability" class="btn btn-sm btn-subtle">
                                <i class="bi bi-plus-circle me-1"></i> Manage Slots
                            </a>
                        </c:if>
                    </div>
                    <div class="p-3">
                        <c:choose>
                            <c:when test="${not empty availability}">
                                <div class="d-flex flex-wrap gap-2">
                                    <c:forEach var="slot" items="${availability}">
                                        <span class="badge bg-light text-dark border p-2 small">
                                            <i class="bi bi-calendar-day me-1 text-primary"></i>
                                            ${slot.dayOfWeek}: ${slot.startTime.toString().substring(0, 5)} - ${slot.endTime.toString().substring(0, 5)}
                                        </span>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small mb-0">No availability slots set yet.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>

            <!-- Right Column: Peer Feedback & Reviews -->
            <div class="col-lg-5">
                <div class="card-custom">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-star-fill text-warning fs-5"></i>
                            <span class="fw-bold">Peer Reviews &amp; Feedback</span>
                        </div>
                        <span class="badge bg-primary-subtle text-primary">${reviews.size()} Total</span>
                    </div>
                    <div class="p-3">
                        <c:choose>
                            <c:when test="${not empty reviews}">
                                <div class="d-flex flex-column gap-3">
                                    <c:forEach var="rev" items="${reviews}">
                                        <div class="p-3 border rounded-3 bg-light">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <div class="d-flex align-items-center gap-2">
                                                    <div class="avatar avatar-sm bg-primary-subtle text-primary">
                                                        ${rev.reviewerName.substring(0, 1)}
                                                    </div>
                                                    <div>
                                                        <div class="fw-bold small text-dark">${rev.reviewerName}</div>
                                                        <div class="text-muted" style="font-size: 0.72rem;">${rev.sessionSkillName} &bull; ${rev.createdAt}</div>
                                                    </div>
                                                </div>
                                                <div class="star-rating small">
                                                    <c:forEach begin="1" end="${rev.rating}">
                                                        <i class="bi bi-star-fill text-warning"></i>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                            <p class="small text-dark mb-0 fst-italic">"${rev.comment}"</p>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4 text-muted small">
                                    <i class="bi bi-chat-heart text-secondary fs-3 d-block mb-2"></i>
                                    No reviews yet. Complete learning sessions to build your peer rating!
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

    </div>
</main>

<!-- Edit Profile Modal -->
<c:if test="${isOwnProfile}">
    <div class="modal fade" id="editProfileModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content border-0 shadow">
                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold">Edit Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="action" value="updateProfile">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label" for="editFullName">Full Name</label>
                            <input type="text" class="form-control" id="editFullName" name="fullName"
                                   value="${profileUser.fullName}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="editDepartment">Department</label>
                            <select class="form-select" id="editDepartment" name="department" required>
                                <option value="Computer Science and Engineering" ${profileUser.department == 'Computer Science and Engineering' ? 'selected' : ''}>Computer Science and Engineering (CSE)</option>
                                <option value="Electronics and Communication Engineering" ${profileUser.department == 'Electronics and Communication Engineering' ? 'selected' : ''}>Electronics and Communication Engineering (ECE)</option>
                                <option value="Artificial Intelligence and Data Science" ${profileUser.department == 'Artificial Intelligence and Data Science' ? 'selected' : ''}>Artificial Intelligence &amp; Data Science (AI &amp; DS)</option>
                                <option value="Electrical and Electronics Engineering" ${profileUser.department == 'Electrical and Electronics Engineering' ? 'selected' : ''}>Electrical and Electronics Engineering (EEE)</option>
                                <option value="Mechanical Engineering" ${profileUser.department == 'Mechanical Engineering' ? 'selected' : ''}>Mechanical Engineering (MECH)</option>
                                <option value="Civil Engineering" ${profileUser.department == 'Civil Engineering' ? 'selected' : ''}>Civil Engineering (CIVIL)</option>
                                <option value="School of Entrepreneurship and Management Studies" ${profileUser.department == 'School of Entrepreneurship and Management Studies' ? 'selected' : ''}>School of Entrepreneurship and Management Studies (SEAMS)</option>
                                <option value="Liberal Arts and Basic Sciences" ${profileUser.department == 'Liberal Arts and Basic Sciences' ? 'selected' : ''}>Liberal Arts &amp; Basic Sciences (SLABS)</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="editYear">Year of Study</label>
                            <select class="form-select" id="editYear" name="yearOfStudy" required>
                                <option value="1" ${profileUser.yearOfStudy == 1 ? 'selected' : ''}>1st Year</option>
                                <option value="2" ${profileUser.yearOfStudy == 2 ? 'selected' : ''}>2nd Year</option>
                                <option value="3" ${profileUser.yearOfStudy == 3 ? 'selected' : ''}>3rd Year</option>
                                <option value="4" ${profileUser.yearOfStudy == 4 ? 'selected' : ''}>4th Year</option>
                                <option value="5" ${profileUser.yearOfStudy == 5 ? 'selected' : ''}>5th Year</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="editBio">Bio &amp; Academic Interests</label>
                            <textarea class="form-control" id="editBio" name="bio" rows="3">${profileUser.bio}</textarea>
                        </div>
                    </div>
                    <div class="modal-footer border-top">
                        <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Change Password Modal -->
    <div class="modal fade" id="changePasswordModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content border-0 shadow">
                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold">Change Password</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="action" value="changePassword">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label" for="currentPassword">Current Password</label>
                            <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="newPassword">New Password (min 6 characters)</label>
                            <input type="password" class="form-control" id="newPassword" name="newPassword" required minlength="6">
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="confirmPassword">Confirm New Password</label>
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required minlength="6">
                        </div>
                    </div>
                    <div class="modal-footer border-top">
                        <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update Password</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</c:if>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

