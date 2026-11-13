<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Student Dashboard" />
<c:set var="activePage" value="dashboard" />
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

        <!-- Welcome Banner -->
        <div class="card-custom p-4 mb-4 bg-white border-0 shadow-sm">
            <div class="row align-items-center g-3">
                <div class="col-auto">
                    <div class="avatar avatar-xl bg-primary text-white">
                        ${sessionScope.user.fullName != null ? sessionScope.user.fullName.substring(0, 1) : 'U'}
                    </div>
                </div>
                <div class="col">
                    <div class="d-flex flex-wrap align-items-center gap-2 mb-1">
                        <h3 class="fw-bold text-dark mb-0">${sessionScope.user.fullName}</h3>
                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle">
                            ${sessionScope.user.studentId}
                        </span>
                        <span class="badge bg-light text-secondary border">
                            Year ${sessionScope.user.yearOfStudy} &bull; ${sessionScope.user.department}
                        </span>
                    </div>
                    <p class="text-muted small mb-0">
                        ${not empty sessionScope.user.bio ? sessionScope.user.bio : 'Welcome to SkillSwap SRM AP! Add the skills you can teach and what you want to learn to unlock smart matches.'}
                    </p>
                </div>
                <div class="col-lg-auto d-flex flex-wrap gap-2">
                    <a href="${pageContext.request.contextPath}/skills" class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-plus-circle me-1"></i> Add Skills
                    </a>
                    <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary btn-sm">
                        <i class="bi bi-stars me-1"></i> Find Matches
                    </a>
                </div>
            </div>

            <!-- Profile Completeness -->
            <div class="mt-4 pt-3 border-top">
                <div class="d-flex justify-content-between align-items-center small mb-1">
                    <span class="fw-bold text-muted">Profile &amp; Skill Completeness</span>
                    <span class="fw-bold text-primary">${profileCompleteness}%</span>
                </div>
                <div class="progress" style="height: 8px;">
                    <div class="progress-bar bg-primary" role="progressbar" style="width: ${profileCompleteness}%;"></div>
                </div>
                <c:if test="${profileCompleteness < 100}">
                    <div class="small text-muted mt-1">
                        <i class="bi bi-info-circle me-1"></i>
                        <c:choose>
                            <c:when test="${empty offeredSkills}">Add skills you can teach to unlock exchange requests.</c:when>
                            <c:when test="${empty wantedSkills}">Add skills you want to learn to receive mutual matches.</c:when>
                            <c:otherwise>Complete your weekly schedule availability in your profile to boost matching scores.</c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- 4 Quick Stat Cards -->
        <div class="row g-3 mb-4">
            <div class="col-lg-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon indigo"><i class="bi bi-coin"></i></div>
                    <div class="stat-value">${creditBalance}</div>
                    <div class="stat-label">Virtual Credits Available</div>
                    <a href="${pageContext.request.contextPath}/credits" class="stretched-link"></a>
                </div>
            </div>
            <div class="col-lg-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon emerald"><i class="bi bi-lightbulb"></i></div>
                    <div class="stat-value">${offeredSkills.size()}</div>
                    <div class="stat-label">Skills You Can Teach</div>
                    <a href="${pageContext.request.contextPath}/skills" class="stretched-link"></a>
                </div>
            </div>
            <div class="col-lg-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon purple"><i class="bi bi-book"></i></div>
                    <div class="stat-value">${wantedSkills.size()}</div>
                    <div class="stat-label">Skills You Want to Learn</div>
                    <a href="${pageContext.request.contextPath}/skills" class="stretched-link"></a>
                </div>
            </div>
            <div class="col-lg-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon amber"><i class="bi bi-bell"></i></div>
                    <div class="stat-value">${pendingRequestsCount}</div>
                    <div class="stat-label">Pending Exchange Requests</div>
                    <a href="${pageContext.request.contextPath}/requests" class="stretched-link"></a>
                </div>
            </div>
        </div>

        <!-- Main Dashboard Split: Recommended Matches & Upcoming Sessions -->
        <div class="row g-4">
            <!-- Left Column: Recommended Matches -->
            <div class="col-lg-8">
                <div class="card-custom mb-4">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-stars text-primary fs-5"></i>
                            <span class="fw-bold">Top Recommended Matches for You</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/matches" class="btn btn-sm btn-subtle">
                            View All Matches &rarr;
                        </a>
                    </div>
                    <div class="p-3">
                        <c:choose>
                            <c:when test="${not empty recommendedMatches}">
                                <div class="row g-3">
                                    <c:forEach var="match" items="${recommendedMatches}">
                                        <div class="col-md-6">
                                            <div class="p-3 border rounded-3 bg-white h-100 d-flex flex-column">
                                                <div class="d-flex justify-content-between align-items-start mb-2">
                                                    <div class="d-flex align-items-center gap-2">
                                                        <div class="avatar avatar-sm bg-primary-subtle text-primary">
                                                            ${match.matchedUser.fullName.substring(0, 1)}
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold text-dark small">${match.matchedUser.fullName}</div>
                                                            <div class="text-muted" style="font-size: 0.75rem;">${match.matchedUser.department}</div>
                                                        </div>
                                                    </div>
                                                    <span class="badge ${match.mutualMatch ? 'badge-mutual' : 'bg-primary-subtle text-primary'}">
                                                        ${match.matchScore}% Match
                                                    </span>
                                                </div>

                                                <c:if test="${match.mutualMatch}">
                                                    <div class="small text-success fw-bold mb-2">
                                                        <i class="bi bi-check2-circle me-1"></i> Mutual Match!
                                                    </div>
                                                </c:if>

                                                <div class="small mb-1">
                                                    <span class="text-muted">Teaches:</span>
                                                    <c:forEach var="sk" items="${match.theyOfferWeWant}">
                                                        <span class="badge badge-custom badge-offer me-1">${sk.skillName}</span>
                                                    </c:forEach>
                                                </div>

                                                <div class="small mb-3">
                                                    <span class="text-muted">Wants:</span>
                                                    <c:forEach var="sk" items="${match.weOfferTheyWant}">
                                                        <span class="badge badge-custom badge-want me-1">${sk.skillName}</span>
                                                    </c:forEach>
                                                </div>

                                                <div class="mt-auto pt-2 border-top d-flex justify-content-between align-items-center">
                                                    <a href="${pageContext.request.contextPath}/profile?id=${match.matchedUser.userId}" class="small text-decoration-none">
                                                        View Profile
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary btn-sm py-1 px-2" style="font-size: 0.8rem;">
                                                        <i class="bi bi-arrow-left-right me-1"></i> Connect
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4 text-muted">
                                    <i class="bi bi-emoji-smile fs-2 text-secondary mb-2 d-block"></i>
                                    <p class="mb-2">No matching recommendations found yet.</p>
                                    <p class="small text-muted">Add more skills you want to learn to get paired with SRM AP peers!</p>
                                    <a href="${pageContext.request.contextPath}/skills" class="btn btn-outline-primary btn-sm">
                                        Manage Your Skills
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Upcoming Scheduled Sessions -->
                <div class="card-custom">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-calendar-event text-primary fs-5"></i>
                            <span class="fw-bold">Upcoming Learning Sessions</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/sessions" class="btn btn-sm btn-subtle">
                            Manage Sessions &rarr;
                        </a>
                    </div>
                    <div class="p-3">
                        <c:choose>
                            <c:when test="${not empty upcomingSessions}">
                                <div class="d-flex flex-column gap-3">
                                    <c:forEach var="sess" items="${upcomingSessions}">
                                        <div class="p-3 border rounded-3 bg-white session-card d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                                            <div>
                                                <div class="d-flex align-items-center gap-2 mb-1">
                                                    <span class="fw-bold text-dark">${sess.skillName}</span>
                                                    <span class="badge ${sess.teacherId == sessionScope.user.userId ? 'badge-offer' : 'badge-want'}">
                                                        ${sess.teacherId == sessionScope.user.userId ? 'You are Teaching' : 'You are Learning'}
                                                    </span>
                                                </div>
                                                <div class="small text-muted">
                                                    <i class="bi bi-person me-1"></i>
                                                    Partner: <strong>${sess.teacherId == sessionScope.user.userId ? sess.learnerName : sess.teacherName}</strong>
                                                    (${sess.teacherId == sessionScope.user.userId ? sess.learnerDepartment : sess.teacherDepartment})
                                                </div>
                                                <div class="small text-secondary mt-1">
                                                    <i class="bi bi-clock me-1"></i> ${sess.sessionDate} &bull; ${sess.startTime} - ${sess.endTime}
                                                    <span class="ms-2"><i class="bi bi-geo-alt me-1"></i> ${sess.meetingMode}: ${sess.meetingDetails}</span>
                                                </div>
                                            </div>
                                            <div>
                                                <a href="${pageContext.request.contextPath}/sessions" class="btn btn-outline-primary btn-sm">
                                                    Session Details
                                                </a>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4 text-muted">
                                    <i class="bi bi-calendar-x fs-2 text-secondary mb-2 d-block"></i>
                                    <p class="mb-1">No upcoming sessions booked.</p>
                                    <p class="small text-muted">Accept an exchange request to schedule a session with a peer.</p>
                                    <a href="${pageContext.request.contextPath}/requests" class="btn btn-outline-primary btn-sm">
                                        Check Exchange Requests
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Right Column: Pending Requests & Recent Notifications -->
            <div class="col-lg-4">
                <!-- Pending Exchange Requests Previews -->
                <div class="card-custom mb-4">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-inbox text-primary fs-5"></i>
                            <span class="fw-bold">Incoming Requests</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/requests" class="small text-decoration-none">View All</a>
                    </div>
                    <div class="p-3">
                        <c:set var="hasPending" value="false" />
                        <c:forEach var="req" items="${receivedRequests}">
                            <c:if test="${req.pending}">
                                <c:set var="hasPending" value="true" />
                                <div class="p-3 border rounded-3 bg-light mb-2">
                                    <div class="d-flex justify-content-between align-items-start mb-1">
                                        <div class="fw-bold small text-dark">${req.senderName}</div>
                                        <span class="badge bg-warning text-dark" style="font-size: 0.7rem;">Pending</span>
                                    </div>
                                    <div class="small text-muted mb-2" style="font-size: 0.78rem;">
                                        Offers: <strong class="text-success">${req.offeredSkillName}</strong><br>
                                        Wants: <strong class="text-primary">${req.requestedSkillName}</strong>
                                    </div>
                                    <div class="d-flex gap-2">
                                        <form action="${pageContext.request.contextPath}/requests" method="POST" class="d-inline">
                                            <input type="hidden" name="action" value="accept">
                                            <input type="hidden" name="requestId" value="${req.requestId}">
                                            <button type="submit" class="btn btn-primary btn-sm py-1 px-2" style="font-size: 0.75rem;">Accept</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/requests" method="POST" class="d-inline">
                                            <input type="hidden" name="action" value="reject">
                                            <input type="hidden" name="requestId" value="${req.requestId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm py-1 px-2" style="font-size: 0.75rem;">Decline</button>
                                        </form>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                        <c:if test="${!hasPending}">
                            <div class="text-center py-3 text-muted small">
                                <i class="bi bi-envelope-open text-secondary fs-4 d-block mb-1"></i>
                                No pending requests at the moment.
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Recent Notifications -->
                <div class="card-custom">
                    <div class="card-header-clean">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-bell text-primary fs-5"></i>
                            <span class="fw-bold">Activity &amp; Alerts</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/notifications" class="small text-decoration-none">All</a>
                    </div>
                    <div class="p-3">
                        <c:choose>
                            <c:when test="${not empty recentNotifications}">
                                <div class="d-flex flex-column gap-2">
                                    <c:forEach var="notif" items="${recentNotifications}">
                                        <div class="p-2 rounded-2 ${notif.read ? 'bg-white' : 'bg-primary-subtle border-start border-primary border-3'} small">
                                            <div class="text-dark">${notif.message}</div>
                                            <div class="text-muted" style="font-size: 0.7rem;">${notif.createdAt}</div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-3 text-muted small">
                                    No recent activity.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

