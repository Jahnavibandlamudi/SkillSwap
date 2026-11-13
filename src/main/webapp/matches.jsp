<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Smart Skill Matches" />
<c:set var="activePage" value="matches" />
<jsp:include page="/WEB-INF/partials/header.jsp" />
<jsp:include page="/WEB-INF/partials/navbar.jsp" />

<main class="py-4 bg-page">
    <div class="container">

        <!-- Flash messages -->
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

        <!-- Header -->
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
            <div>
                <h3 class="fw-bold text-dark mb-1">
                    <i class="bi bi-stars text-primary me-1"></i> Intelligent Skill Matching
                </h3>
                <p class="text-muted small mb-0">
                    Proprietary algorithm matching complementary teaching skills, learning interests, and schedule availability across SRM University AP.
                </p>
            </div>
            <div>
                <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-2">
                    <i class="bi bi-check2-all me-1"></i> ${matches.size()} Compatible Students Found
                </span>
            </div>
        </div>

        <!-- Filter Controls -->
        <div class="card-custom p-3 mb-4 bg-white">
            <form action="${pageContext.request.contextPath}/matches" method="GET" class="row g-2 align-items-center">
                <div class="col-md-5">
                    <div class="input-group">
                        <span class="input-group-text bg-light text-muted border-end-0"><i class="bi bi-search"></i></span>
                        <input type="text" class="form-control border-start-0" name="q" value="${searchQuery}"
                               placeholder="Search candidate by name, department, or student ID...">
                    </div>
                </div>
                <div class="col-md-3">
                    <select class="form-select" name="category" onchange="this.form.submit()">
                        <option value="">All Categories</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat}" ${selectedCategory == cat ? 'selected' : ''}>${cat}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <div class="form-check form-switch pt-2">
                        <input class="form-check-input" type="checkbox" id="mutualSwitch" name="mutualOnly" value="true"
                               ${mutualOnly ? 'checked' : ''} onchange="this.form.submit()">
                        <label class="form-check-label small fw-semibold" for="mutualSwitch">Mutual Only</label>
                    </div>
                </div>
                <div class="col-md-2 d-flex gap-2">
                    <button type="submit" class="btn btn-primary flex-fill">Apply</button>
                    <c:if test="${not empty selectedCategory || not empty searchQuery || mutualOnly}">
                        <a href="${pageContext.request.contextPath}/matches" class="btn btn-subtle" title="Reset Filters">
                            <i class="bi bi-arrow-counterclockwise"></i>
                        </a>
                    </c:if>
                </div>
            </form>
        </div>

        <!-- Matches Grid -->
        <div class="row g-4">
            <c:choose>
                <c:when test="${not empty matches}">
                    <c:forEach var="mr" items="${matches}">
                        <div class="col-lg-6">
                            <div class="card-custom p-4 h-100 d-flex flex-column bg-white ${mr.mutualMatch ? 'border-primary border-2' : ''}">
                                
                                <!-- Card Header: User Info & Match Score -->
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="avatar avatar-lg bg-primary text-white shadow-sm">
                                            ${mr.matchedUser.fullName.substring(0, 1)}
                                        </div>
                                        <div>
                                            <div class="d-flex align-items-center gap-2">
                                                <h5 class="fw-bold text-dark mb-0">${mr.matchedUser.fullName}</h5>
                                                <span class="badge bg-light text-secondary border small">${mr.matchedUser.studentId}</span>
                                            </div>
                                            <div class="small text-muted mt-1">
                                                ${mr.matchedUser.department} &bull; Year ${mr.matchedUser.yearOfStudy}
                                            </div>
                                            <div class="star-rating small mt-1">
                                                <i class="bi bi-star-fill text-warning"></i>
                                                <strong class="text-dark ms-1">${mr.matchedUser.averageRating > 0 ? mr.matchedUser.averageRating : 'New'}</strong>
                                                <span class="text-muted ms-1">(${mr.matchedUser.completedSessionsCount} sessions completed)</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="text-end">
                                        <div class="match-score-pill mb-1">
                                            ${mr.matchScore}% Match
                                        </div>
                                        <c:if test="${mr.mutualMatch}">
                                            <div>
                                                <span class="badge badge-custom badge-mutual">
                                                    <i class="bi bi-stars"></i> Mutual
                                                </span>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Match Summary Banner -->
                                <div class="p-2 px-3 rounded-2 ${mr.mutualMatch ? 'bg-success-subtle text-success border border-success-subtle' : 'bg-light text-secondary'} small mb-3">
                                    <i class="bi ${mr.mutualMatch ? 'bi-check-circle-fill' : 'bi-info-circle'} me-1"></i>
                                    ${mr.matchSummary}
                                </div>

                                <!-- Skills Exchange Comparison -->
                                <div class="row g-2 mb-3">
                                    <div class="col-sm-6">
                                        <div class="p-3 bg-light rounded-3 h-100">
                                            <div class="small fw-bold text-uppercase text-muted mb-2">
                                                <i class="bi bi-mortarboard text-success me-1"></i> They Teach (You Want)
                                            </div>
                                            <c:choose>
                                                <c:when test="${not empty mr.theyOfferWeWant}">
                                                    <div class="d-flex flex-wrap gap-1">
                                                        <c:forEach var="sk" items="${mr.theyOfferWeWant}">
                                                            <span class="badge badge-custom badge-offer">
                                                                ${sk.skillName}
                                                                <span class="badge-proficiency ms-1">${sk.proficiency}</span>
                                                            </span>
                                                        </c:forEach>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted small">None matching your wish list</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="col-sm-6">
                                        <div class="p-3 bg-light rounded-3 h-100">
                                            <div class="small fw-bold text-uppercase text-muted mb-2">
                                                <i class="bi bi-book text-primary me-1"></i> You Teach (They Want)
                                            </div>
                                            <c:choose>
                                                <c:when test="${not empty mr.weOfferTheyWant}">
                                                    <div class="d-flex flex-wrap gap-1">
                                                        <c:forEach var="sk" items="${mr.weOfferTheyWant}">
                                                            <span class="badge badge-custom badge-want">
                                                                ${sk.skillName}
                                                                <span class="badge-proficiency ms-1">${sk.proficiency}</span>
                                                            </span>
                                                        </c:forEach>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted small">One-way learning</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>

                                <!-- Availability Overlap -->
                                <c:if test="${not empty mr.overlappingDays}">
                                    <div class="small text-muted mb-3">
                                        <i class="bi bi-calendar-check text-primary me-1"></i>
                                        Overlapping Available Days:
                                        <c:forEach var="d" items="${mr.overlappingDays}">
                                            <span class="badge bg-light text-dark border me-1">${d}</span>
                                        </c:forEach>
                                    </div>
                                </c:if>

                                <!-- Footer Actions -->
                                <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                    <a href="${pageContext.request.contextPath}/profile?id=${mr.matchedUser.userId}" class="btn btn-subtle btn-sm">
                                        <i class="bi bi-person me-1"></i> View Full Profile
                                    </a>
                                    <button type="button" class="btn btn-primary btn-sm"
                                            onclick="openRequestModal(${mr.matchedUser.userId}, '${mr.matchedUser.fullName}',
                                                     ${not empty mr.weOfferTheyWant ? mr.weOfferTheyWant[0].skillId : (not empty myOffered ? myOffered[0].skillId : 0)},
                                                     ${not empty mr.theyOfferWeWant ? mr.theyOfferWeWant[0].skillId : (not empty myWanted ? myWanted[0].skillId : 0)})">
                                        <i class="bi bi-arrow-left-right me-1"></i> Propose Exchange
                                    </button>
                                </div>

                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-search text-secondary fs-1 d-block mb-3"></i>
                        <h4 class="fw-bold text-dark">No matching skill partners found</h4>
                        <p class="text-muted small mb-4">
                            Expand your horizons! Add more skills you can teach or topics you are excited to learn to discover compatible SRM AP peers.
                        </p>
                        <a href="${pageContext.request.contextPath}/skills" class="btn btn-primary">
                            <i class="bi bi-plus-circle me-1"></i> Manage My Skills
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</main>

<!-- Propose Skill Exchange Request Modal -->
<div class="modal fade" id="sendRequestModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold">Propose Skill Exchange</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/requests" method="POST">
                <input type="hidden" name="action" value="send">
                <input type="hidden" id="reqReceiverId" name="receiverId" value="">

                <div class="modal-body">
                    <div class="p-3 bg-light rounded-3 mb-3">
                        <div class="small text-muted text-uppercase fw-bold">Exchange Partner</div>
                        <h5 class="fw-bold text-dark mb-0" id="reqReceiverName">Student Name</h5>
                    </div>

                    <!-- Skill You Will Teach -->
                    <div class="mb-3">
                        <label class="form-label" for="reqOfferedSkill">Skill You Will Teach (Offered Skill) <span class="text-danger">*</span></label>
                        <select class="form-select" id="reqOfferedSkill" name="offeredSkillId" required>
                            <c:forEach var="os" items="${myOffered}">
                                <option value="${os.skillId}">${os.skillName} (${os.proficiency})</option>
                            </c:forEach>
                        </select>
                        <c:if test="${empty myOffered}">
                            <div class="form-text text-danger small">You have not listed any teaching skills yet. Please add a teaching skill first.</div>
                        </c:if>
                    </div>

                    <!-- Skill You Want to Learn -->
                    <div class="mb-3">
                        <label class="form-label" for="reqRequestedSkill">Skill You Want to Learn (Requested Skill) <span class="text-danger">*</span></label>
                        <select class="form-select" id="reqRequestedSkill" name="requestedSkillId" required>
                            <c:forEach var="ws" items="${myWanted}">
                                <option value="${ws.skillId}">${ws.skillName}</option>
                            </c:forEach>
                        </select>
                        <c:if test="${empty myWanted}">
                            <div class="form-text text-danger small">You have not listed any learning interests yet. Please add a learning interest first.</div>
                        </c:if>
                    </div>

                    <!-- Optional Note / Message -->
                    <div class="mb-3">
                        <label class="form-label" for="reqMessage">Introduction Message (Optional)</label>
                        <textarea class="form-control" id="reqMessage" name="message" rows="3"
                                  placeholder="Hi! I would love to exchange knowledge with you on campus or online. Let's learn together!"></textarea>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary" ${empty myOffered ? 'disabled' : ''}>
                        <i class="bi bi-send me-1"></i> Send Exchange Proposal
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function openRequestModal(receiverId, receiverName, offeredSkillId, requestedSkillId) {
    document.getElementById('reqReceiverId').value = receiverId;
    document.getElementById('reqReceiverName').textContent = receiverName;
    if (offeredSkillId > 0) {
        document.getElementById('reqOfferedSkill').value = offeredSkillId;
    }
    if (requestedSkillId > 0) {
        document.getElementById('reqRequestedSkill').value = requestedSkillId;
    }
    const modal = new bootstrap.Modal(document.getElementById('sendRequestModal'));
    modal.show();
}
</script>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

