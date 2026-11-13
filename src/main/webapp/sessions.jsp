<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Learning Sessions" />
<c:set var="activePage" value="sessions" />
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
                <h3 class="fw-bold text-dark mb-1">Learning Sessions</h3>
                <p class="text-muted small mb-0">Coordinate mutual learning sessions, confirm completion together, and earn virtual platform credits.</p>
            </div>
            <div>
                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#scheduleSessionModal">
                    <i class="bi bi-calendar-plus me-1"></i> Schedule Learning Session
                </button>
            </div>
        </div>

        <!-- Session Tabs -->
        <ul class="nav nav-pills mb-4 bg-white p-2 rounded-3 shadow-sm border" id="sessionTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active fw-bold small" id="upcoming-tab" data-bs-toggle="pill" data-bs-target="#upcoming" type="button" role="tab">
                    <i class="bi bi-calendar-check me-1"></i> Upcoming Sessions
                    <span class="badge bg-primary-subtle text-primary ms-1">${upcomingSessions.size()}</span>
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold small" id="completed-tab" data-bs-toggle="pill" data-bs-target="#completed" type="button" role="tab">
                    <i class="bi bi-patch-check me-1"></i> Completed Sessions
                    <span class="badge bg-success-subtle text-success ms-1">${completedSessions.size()}</span>
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold small" id="all-tab" data-bs-toggle="pill" data-bs-target="#all" type="button" role="tab">
                    <i class="bi bi-collection me-1"></i> All Sessions History
                    <span class="badge bg-secondary-subtle text-secondary ms-1">${allSessions.size()}</span>
                </button>
            </li>
        </ul>

        <!-- Tab Content -->
        <div class="tab-content" id="sessionTabsContent">
            
            <!-- 1. Upcoming Sessions Tab -->
            <div class="tab-pane fade show active" id="upcoming" role="tabpanel">
                <c:choose>
                    <c:when test="${not empty upcomingSessions}">
                        <div class="row g-4">
                            <c:forEach var="sess" items="${upcomingSessions}">
                                <div class="col-lg-6">
                                    <div class="card-custom p-4 bg-white session-card h-100 d-flex flex-column">
                                        <div class="d-flex justify-content-between align-items-start mb-3">
                                            <div>
                                                <span class="badge ${sess.teacherId == sessionScope.user.userId ? 'badge-offer' : 'badge-want'} mb-1">
                                                    <i class="bi ${sess.teacherId == sessionScope.user.userId ? 'bi-mortarboard' : 'bi-book'} me-1"></i>
                                                    ${sess.teacherId == sessionScope.user.userId ? 'Teaching Role' : 'Learner Role'}
                                                </span>
                                                <h5 class="fw-bold text-dark mb-0">${sess.skillName}</h5>
                                            </div>
                                            <span class="badge bg-primary-subtle text-primary border border-primary-subtle">
                                                ${sess.status}
                                            </span>
                                        </div>

                                        <div class="p-3 bg-light rounded-3 mb-3 small">
                                            <div class="mb-1">
                                                <span class="text-muted">Partner:</span>
                                                <strong class="text-dark">
                                                    ${sess.teacherId == sessionScope.user.userId ? sess.learnerName : sess.teacherName}
                                                </strong>
                                                <span class="text-secondary">(${sess.teacherId == sessionScope.user.userId ? sess.learnerDepartment : sess.teacherDepartment})</span>
                                            </div>
                                            <div class="mb-1">
                                                <span class="text-muted"><i class="bi bi-clock me-1 text-primary"></i> Time:</span>
                                                <strong>${sess.sessionDate} &bull; ${sess.startTime} - ${sess.endTime}</strong>
                                            </div>
                                            <div>
                                                <span class="text-muted"><i class="bi bi-geo-alt me-1 text-primary"></i> ${sess.meetingMode}:</span>
                                                <strong class="text-primary">${sess.meetingDetails}</strong>
                                            </div>
                                        </div>

                                        <!-- Two-Way Confirmation Status -->
                                        <div class="p-2 px-3 border rounded-3 bg-white mb-3 small">
                                            <div class="fw-bold text-muted mb-2" style="font-size: 0.75rem;">COMPLETION CONFIRMATION STATUS</div>
                                            <div class="d-flex justify-content-between align-items-center mb-1">
                                                <span>Teacher (${sess.teacherName}):</span>
                                                <c:choose>
                                                    <c:when test="${sess.teacherConfirmed}">
                                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-lg"></i> Confirmed</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary-subtle text-secondary">Awaiting Confirmation</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <span>Learner (${sess.learnerName}):</span>
                                                <c:choose>
                                                    <c:when test="${sess.learnerConfirmed}">
                                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-lg"></i> Confirmed</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary-subtle text-secondary">Awaiting Confirmation</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>

                                        <!-- Actions -->
                                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                            <c:set var="myConfirmed" value="${sess.teacherId == sessionScope.user.userId ? sess.teacherConfirmed : sess.learnerConfirmed}" />
                                            <c:choose>
                                                <c:when test="${!myConfirmed}">
                                                    <form action="${pageContext.request.contextPath}/sessions" method="POST" class="d-inline">
                                                        <input type="hidden" name="action" value="confirm">
                                                        <input type="hidden" name="sessionId" value="${sess.sessionId}">
                                                        <button type="submit" class="btn btn-success btn-sm">
                                                            <i class="bi bi-check2-circle me-1"></i> Confirm Session Completed
                                                        </button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-success-subtle text-success p-2 small">
                                                        <i class="bi bi-check2 me-1"></i> Your completion recorded
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>

                                            <form action="${pageContext.request.contextPath}/sessions" method="POST" class="d-inline"
                                                  onsubmit="return confirm('Are you sure you want to cancel this scheduled session?');">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="sessionId" value="${sess.sessionId}">
                                                <button type="submit" class="btn btn-outline-danger btn-sm">
                                                    Cancel
                                                </button>
                                            </form>
                                        </div>

                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card-custom p-5 text-center bg-white">
                            <i class="bi bi-calendar3 text-secondary fs-1 d-block mb-2"></i>
                            <h5 class="fw-bold text-dark">No upcoming sessions</h5>
                            <p class="text-muted small mb-3">Schedule a session from your accepted exchange requests to start learning.</p>
                            <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#scheduleSessionModal">
                                Schedule Learning Session
                            </button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 2. Completed Sessions Tab -->
            <div class="tab-pane fade" id="completed" role="tabpanel">
                <c:choose>
                    <c:when test="${not empty completedSessions}">
                        <div class="row g-4">
                            <c:forEach var="sess" items="${completedSessions}">
                                <div class="col-lg-6">
                                    <div class="card-custom p-4 bg-white session-card completed h-100 d-flex flex-column">
                                        <div class="d-flex justify-content-between align-items-start mb-3">
                                            <div>
                                                <span class="badge bg-success-subtle text-success border border-success-subtle mb-1">
                                                    <i class="bi bi-check-all me-1"></i> Both Confirmed &bull; Completed
                                                </span>
                                                <h5 class="fw-bold text-dark mb-0">${sess.skillName}</h5>
                                            </div>
                                            <span class="badge ${sess.teacherId == sessionScope.user.userId ? 'bg-primary' : 'bg-secondary'}">
                                                ${sess.teacherId == sessionScope.user.userId ? '+2 Credits Earned' : '-2 Credits Spent'}
                                            </span>
                                        </div>

                                        <div class="p-3 bg-light rounded-3 mb-3 small">
                                            <div class="mb-1">
                                                <span class="text-muted">Partner:</span>
                                                <strong class="text-dark">
                                                    ${sess.teacherId == sessionScope.user.userId ? sess.learnerName : sess.teacherName}
                                                </strong>
                                            </div>
                                            <div>
                                                <span class="text-muted"><i class="bi bi-calendar-event me-1"></i> Date:</span>
                                                ${sess.sessionDate} (${sess.startTime} - ${sess.endTime})
                                            </div>
                                        </div>

                                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                            <c:choose>
                                                <c:when test="${sess.userHasReviewed}">
                                                    <span class="small text-success fw-bold">
                                                        <i class="bi bi-check-circle-fill me-1"></i> Review Submitted
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button" class="btn btn-warning btn-sm text-dark fw-bold"
                                                            onclick="openReviewModal(${sess.sessionId}, '${sess.skillName}', '${sess.teacherId == sessionScope.user.userId ? sess.learnerName : sess.teacherName}')">
                                                        <i class="bi bi-star-fill me-1"></i> Rate &amp; Review Partner
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                            <span class="text-muted small" style="font-size: 0.72rem;">Session ID #${sess.sessionId}</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card-custom p-5 text-center bg-white">
                            <i class="bi bi-patch-check text-secondary fs-1 d-block mb-2"></i>
                            <h5 class="fw-bold text-dark">No completed sessions yet</h5>
                            <p class="text-muted small mb-0">When both you and your partner confirm a scheduled session, it will show up here and unlock peer review.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 3. All Sessions History Tab -->
            <div class="tab-pane fade" id="all" role="tabpanel">
                <div class="card-custom bg-white p-3">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle small mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>Session ID</th>
                                    <th>Skill</th>
                                    <th>Your Role</th>
                                    <th>Partner</th>
                                    <th>Date &amp; Time</th>
                                    <th>Mode</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="sess" items="${allSessions}">
                                    <tr>
                                        <td>#${sess.sessionId}</td>
                                        <td class="fw-bold text-dark">${sess.skillName}</td>
                                        <td>
                                            <span class="badge ${sess.teacherId == sessionScope.user.userId ? 'badge-offer' : 'badge-want'}">
                                                ${sess.teacherId == sessionScope.user.userId ? 'Teacher' : 'Learner'}
                                            </span>
                                        </td>
                                        <td>${sess.teacherId == sessionScope.user.userId ? sess.learnerName : sess.teacherName}</td>
                                        <td>${sess.sessionDate} (${sess.startTime.toString().substring(0, 5)})</td>
                                        <td>${sess.meetingMode}</td>
                                        <td>
                                            <span class="badge ${sess.completed ? 'bg-success' : (sess.scheduled ? 'bg-primary' : 'bg-secondary')}">
                                                ${sess.status}
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>

    </div>
</main>

<!-- Schedule Session Modal -->
<div class="modal fade" id="scheduleSessionModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold">Schedule a Learning Session</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/sessions" method="POST">
                <input type="hidden" name="action" value="schedule">
                
                <div class="modal-body">
                    <!-- Select Accepted Exchange Request -->
                    <div class="mb-3">
                        <label class="form-label">Linked Accepted Exchange <span class="text-danger">*</span></label>
                        <select class="form-select" id="exchangeSelect" name="requestId" required onchange="updateExchangeDetails(this)">
                            <option value="">Select Accepted Exchange Partnership...</option>
                            <c:forEach var="req" items="${acceptedRequests}">
                                <option value="${req.requestId}"
                                        data-sender-id="${req.senderId}"
                                        data-sender-name="${req.senderName}"
                                        data-receiver-id="${req.receiverId}"
                                        data-receiver-name="${req.receiverName}"
                                        data-offered-skill-id="${req.offeredSkillId}"
                                        data-offered-skill-name="${req.offeredSkillName}"
                                        data-requested-skill-id="${req.requestedSkillId}"
                                        data-requested-skill-name="${req.requestedSkillName}"
                                        ${prefillRequestId == req.requestId ? 'selected' : ''}>
                                    With ${req.senderId == sessionScope.user.userId ? req.receiverName : req.senderName} (${req.offeredSkillName} &bull; ${req.requestedSkillName})
                                </option>
                            </c:forEach>
                        </select>
                        <c:if test="${empty acceptedRequests}">
                            <div class="form-text text-danger small">You must have an accepted exchange proposal before scheduling sessions.</div>
                        </c:if>
                    </div>

                    <div class="row g-3 mb-3">
                        <!-- Who will teach this session? -->
                        <div class="col-md-6">
                            <label class="form-label">Teacher for this Session <span class="text-danger">*</span></label>
                            <select class="form-select" id="teacherSelect" name="teacherId" required>
                                <option value="${sessionScope.user.userId}">I will Teach (You: ${sessionScope.user.fullName})</option>
                            </select>
                        </div>

                        <!-- Who is learning? -->
                        <div class="col-md-6">
                            <label class="form-label">Learner for this Session <span class="text-danger">*</span></label>
                            <select class="form-select" id="learnerSelect" name="learnerId" required>
                                <option value="">Select Partner as Learner</option>
                            </select>
                        </div>
                    </div>

                    <!-- Topic / Skill -->
                    <div class="mb-3">
                        <label class="form-label">Topic / Skill Being Taught <span class="text-danger">*</span></label>
                        <select class="form-select" id="skillSelect" name="skillId" required>
                            <option value="">Select Skill...</option>
                        </select>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label class="form-label">Session Date <span class="text-danger">*</span></label>
                            <input type="date" class="form-control" name="sessionDate" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Start Time <span class="text-danger">*</span></label>
                            <input type="time" class="form-control" name="startTime" value="17:00" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">End Time <span class="text-danger">*</span></label>
                            <input type="time" class="form-control" name="endTime" value="18:30" required>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label class="form-label">Meeting Mode <span class="text-danger">*</span></label>
                            <select class="form-select" name="meetingMode" required>
                                <option value="ONLINE">ONLINE (Google Meet / Zoom)</option>
                                <option value="OFFLINE">OFFLINE (SRM AP Campus)</option>
                            </select>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label">Meeting Link or SRM AP Location <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="meetingDetails"
                                   placeholder="e.g. meet.google.com/xyz-abc or Central Library 2nd Floor Pod 4" required>
                        </div>
                    </div>

                    <div class="p-3 bg-light rounded-3 small text-muted">
                        <i class="bi bi-info-circle me-1 text-primary"></i>
                        Learner must have at least 2 credits in their wallet to book. 2 credits will be transferred automatically after both students confirm completion.
                    </div>
                </div>

                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary" ${empty acceptedRequests ? 'disabled' : ''}>
                        Schedule &amp; Notify Partner
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Rate & Review Modal -->
<div class="modal fade" id="reviewModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold">Leave Feedback &amp; Rating</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/reviews" method="POST">
                <input type="hidden" id="reviewSessionId" name="sessionId" value="">
                
                <div class="modal-body">
                    <div class="p-3 bg-light rounded-3 mb-3">
                        <div class="small text-muted text-uppercase fw-bold">Partner Feedback</div>
                        <h6 class="fw-bold text-dark mb-0" id="reviewPartnerName">Partner Name</h6>
                        <div class="small text-secondary" id="reviewSkillName">Skill</div>
                    </div>

                    <!-- Star Selector -->
                    <div class="mb-3 text-center">
                        <label class="form-label d-block">Rating (1 to 5 Stars)</label>
                        <div class="star-rating-selector fs-3 d-inline-flex gap-2 cursor-pointer">
                            <i class="bi bi-star-fill text-warning star-icon" data-value="1"></i>
                            <i class="bi bi-star-fill text-warning star-icon" data-value="2"></i>
                            <i class="bi bi-star-fill text-warning star-icon" data-value="3"></i>
                            <i class="bi bi-star-fill text-warning star-icon" data-value="4"></i>
                            <i class="bi bi-star-fill text-warning star-icon" data-value="5"></i>
                            <input type="hidden" name="rating" value="5">
                        </div>
                    </div>

                    <!-- Comment -->
                    <div class="mb-3">
                        <label class="form-label" for="reviewComment">Review Comment</label>
                        <textarea class="form-control" id="reviewComment" name="comment" rows="3"
                                  placeholder="How was your learning experience? Share helpful feedback for your peer mentor." required></textarea>
                    </div>
                </div>

                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-warning fw-bold text-dark">Submit Review</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function openReviewModal(sessionId, skillName, partnerName) {
    document.getElementById('reviewSessionId').value = sessionId;
    document.getElementById('reviewSkillName').textContent = "Session: " + skillName;
    document.getElementById('reviewPartnerName').textContent = partnerName;
    const modal = new bootstrap.Modal(document.getElementById('reviewModal'));
    modal.show();
}

function updateExchangeDetails(select) {
    const opt = select.options[select.selectedIndex];
    if (!opt || !opt.value) return;

    const senderId = opt.getAttribute('data-sender-id');
    const senderName = opt.getAttribute('data-sender-name');
    const receiverId = opt.getAttribute('data-receiver-id');
    const receiverName = opt.getAttribute('data-receiver-name');
    const offeredSkillId = opt.getAttribute('data-offered-skill-id');
    const offeredSkillName = opt.getAttribute('data-offered-skill-name');
    const requestedSkillId = opt.getAttribute('data-requested-skill-id');
    const requestedSkillName = opt.getAttribute('data-requested-skill-name');

    const teacherSelect = document.getElementById('teacherSelect');
    const learnerSelect = document.getElementById('learnerSelect');
    const skillSelect = document.getElementById('skillSelect');

    teacherSelect.innerHTML = '<option value="' + senderId + '">' + senderName + '</option>' +
                              '<option value="' + receiverId + '">' + receiverName + '</option>';
    learnerSelect.innerHTML = '<option value="' + receiverId + '">' + receiverName + '</option>' +
                              '<option value="' + senderId + '">' + senderName + '</option>';

    skillSelect.innerHTML = '<option value="' + offeredSkillId + '">' + offeredSkillName + '</option>' +
                            '<option value="' + requestedSkillId + '">' + requestedSkillName + '</option>';
}

// Auto-trigger if prefill is active
document.addEventListener('DOMContentLoaded', function() {
    const exchangeSelect = document.getElementById('exchangeSelect');
    if (exchangeSelect && exchangeSelect.value) {
        updateExchangeDetails(exchangeSelect);
    }
});
</script>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

