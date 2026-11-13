<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Skill Exchange Requests" />
<c:set var="activePage" value="requests" />
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
                <h3 class="fw-bold text-dark mb-1">Skill Exchange Requests</h3>
                <p class="text-muted small mb-0">Manage incoming peer proposals, track outgoing requests, and start scheduling sessions.</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> New Exchange Proposal
                </a>
            </div>
        </div>

        <!-- Request Tabs -->
        <ul class="nav nav-pills mb-4 bg-white p-2 rounded-3 shadow-sm border" id="requestTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active fw-bold small" id="received-tab" data-bs-toggle="pill" data-bs-target="#received" type="button" role="tab">
                    <i class="bi bi-inbox me-1"></i> Received Requests
                    <span class="badge bg-secondary-subtle text-secondary ms-1">${receivedRequests.size()}</span>
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold small" id="sent-tab" data-bs-toggle="pill" data-bs-target="#sent" type="button" role="tab">
                    <i class="bi bi-send me-1"></i> Sent Requests
                    <span class="badge bg-secondary-subtle text-secondary ms-1">${sentRequests.size()}</span>
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold small" id="accepted-tab" data-bs-toggle="pill" data-bs-target="#accepted" type="button" role="tab">
                    <i class="bi bi-check2-circle me-1"></i> Accepted Exchanges (Ready to Schedule)
                    <span class="badge bg-success-subtle text-success ms-1">${acceptedRequests.size()}</span>
                </button>
            </li>
        </ul>

        <!-- Tab Content -->
        <div class="tab-content" id="requestTabsContent">
            
            <!-- 1. Received Requests Tab -->
            <div class="tab-pane fade show active" id="received" role="tabpanel">
                <c:choose>
                    <c:when test="${not empty receivedRequests}">
                        <div class="row g-4">
                            <c:forEach var="req" items="${receivedRequests}">
                                <div class="col-lg-6">
                                    <div class="card-custom p-4 bg-white h-100 d-flex flex-column">
                                        <div class="d-flex justify-content-between align-items-start mb-3">
                                            <div class="d-flex align-items-center gap-3">
                                                <div class="avatar avatar-md bg-primary-subtle text-primary">
                                                    ${req.senderName.substring(0, 1)}
                                                </div>
                                                <div>
                                                    <h5 class="fw-bold text-dark mb-0">${req.senderName}</h5>
                                                    <div class="small text-muted">${req.senderDepartment} &bull; ${req.senderEmail}</div>
                                                </div>
                                            </div>
                                            <span class="badge ${req.pending ? 'bg-warning text-dark' : (req.accepted ? 'bg-success' : 'bg-secondary')}">
                                                ${req.status}
                                            </span>
                                        </div>

                                        <div class="p-3 bg-light rounded-3 mb-3 small">
                                            <div class="d-flex justify-content-between mb-1">
                                                <span class="text-muted">They Offer to Teach You:</span>
                                                <strong class="text-success">${req.offeredSkillName}</strong>
                                            </div>
                                            <div class="d-flex justify-content-between">
                                                <span class="text-muted">They Want You to Teach:</span>
                                                <strong class="text-primary">${req.requestedSkillName}</strong>
                                            </div>
                                        </div>

                                        <c:if test="${not empty req.message}">
                                            <p class="small text-dark mb-3 fst-italic">"${req.message}"</p>
                                        </c:if>

                                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                            <span class="text-muted small" style="font-size: 0.75rem;">
                                                <i class="bi bi-clock me-1"></i> ${req.createdAt}
                                            </span>
                                            <c:if test="${req.pending}">
                                                <div class="d-flex gap-2">
                                                    <form action="${pageContext.request.contextPath}/requests" method="POST" class="d-inline">
                                                        <input type="hidden" name="action" value="accept">
                                                        <input type="hidden" name="requestId" value="${req.requestId}">
                                                        <button type="submit" class="btn btn-success btn-sm">
                                                            <i class="bi bi-check-lg me-1"></i> Accept
                                                        </button>
                                                    </form>
                                                    <form action="${pageContext.request.contextPath}/requests" method="POST" class="d-inline">
                                                        <input type="hidden" name="action" value="reject">
                                                        <input type="hidden" name="requestId" value="${req.requestId}">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm">
                                                            <i class="bi bi-x-lg me-1"></i> Decline
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:if>
                                            <c:if test="${req.accepted}">
                                                <a href="${pageContext.request.contextPath}/sessions?requestId=${req.requestId}" class="btn btn-primary btn-sm">
                                                    <i class="bi bi-calendar-plus me-1"></i> Schedule Session
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card-custom p-5 text-center bg-white">
                            <i class="bi bi-inbox text-secondary fs-1 d-block mb-2"></i>
                            <h5 class="fw-bold text-dark">No received requests yet</h5>
                            <p class="text-muted small mb-0">When fellow SRM AP students propose an exchange with your skills, they will show up here.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 2. Sent Requests Tab -->
            <div class="tab-pane fade" id="sent" role="tabpanel">
                <c:choose>
                    <c:when test="${not empty sentRequests}">
                        <div class="row g-4">
                            <c:forEach var="req" items="${sentRequests}">
                                <div class="col-lg-6">
                                    <div class="card-custom p-4 bg-white h-100 d-flex flex-column">
                                        <div class="d-flex justify-content-between align-items-start mb-3">
                                            <div class="d-flex align-items-center gap-3">
                                                <div class="avatar avatar-md bg-secondary-subtle text-secondary">
                                                    ${req.receiverName.substring(0, 1)}
                                                </div>
                                                <div>
                                                    <h5 class="fw-bold text-dark mb-0">${req.receiverName}</h5>
                                                    <div class="small text-muted">${req.receiverDepartment} &bull; ${req.receiverEmail}</div>
                                                </div>
                                            </div>
                                            <span class="badge ${req.pending ? 'bg-warning text-dark' : (req.accepted ? 'bg-success' : 'bg-secondary')}">
                                                ${req.status}
                                            </span>
                                        </div>

                                        <div class="p-3 bg-light rounded-3 mb-3 small">
                                            <div class="d-flex justify-content-between mb-1">
                                                <span class="text-muted">You Offered to Teach:</span>
                                                <strong class="text-success">${req.offeredSkillName}</strong>
                                            </div>
                                            <div class="d-flex justify-content-between">
                                                <span class="text-muted">You Requested to Learn:</span>
                                                <strong class="text-primary">${req.requestedSkillName}</strong>
                                            </div>
                                        </div>

                                        <c:if test="${not empty req.message}">
                                            <p class="small text-dark mb-3 fst-italic">"${req.message}"</p>
                                        </c:if>

                                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                            <span class="text-muted small" style="font-size: 0.75rem;">
                                                <i class="bi bi-clock me-1"></i> Sent on ${req.createdAt}
                                            </span>
                                            <c:if test="${req.pending}">
                                                <form action="${pageContext.request.contextPath}/requests" method="POST" class="d-inline">
                                                    <input type="hidden" name="action" value="cancel">
                                                    <input type="hidden" name="requestId" value="${req.requestId}">
                                                    <button type="submit" class="btn btn-outline-secondary btn-sm">
                                                        <i class="bi bi-x-circle me-1"></i> Cancel Proposal
                                                    </button>
                                                </form>
                                            </c:if>
                                            <c:if test="${req.accepted}">
                                                <a href="${pageContext.request.contextPath}/sessions?requestId=${req.requestId}" class="btn btn-primary btn-sm">
                                                    <i class="bi bi-calendar-plus me-1"></i> Schedule Session
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card-custom p-5 text-center bg-white">
                            <i class="bi bi-send text-secondary fs-1 d-block mb-2"></i>
                            <h5 class="fw-bold text-dark">You have not sent any requests yet</h5>
                            <p class="text-muted small mb-3">Explore smart matches to find compatible partners and propose an exchange!</p>
                            <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary btn-sm">Browse Smart Matches</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 3. Accepted Exchanges Tab -->
            <div class="tab-pane fade" id="accepted" role="tabpanel">
                <c:choose>
                    <c:when test="${not empty acceptedRequests}">
                        <div class="row g-4">
                            <c:forEach var="req" items="${acceptedRequests}">
                                <div class="col-lg-6">
                                    <div class="card-custom p-4 bg-white border-success border-2 h-100 d-flex flex-column">
                                        <div class="d-flex justify-content-between align-items-start mb-3">
                                            <div>
                                                <span class="badge bg-success-subtle text-success border border-success-subtle mb-1">
                                                    <i class="bi bi-check-circle-fill me-1"></i> Accepted Partnership
                                                </span>
                                                <h5 class="fw-bold text-dark mb-0">
                                                    ${req.senderId == sessionScope.user.userId ? req.receiverName : req.senderName}
                                                </h5>
                                                <div class="small text-muted">
                                                    ${req.senderId == sessionScope.user.userId ? req.receiverDepartment : req.senderDepartment}
                                                </div>
                                            </div>
                                            <a href="${pageContext.request.contextPath}/sessions?requestId=${req.requestId}" class="btn btn-primary btn-sm">
                                                <i class="bi bi-calendar-plus me-1"></i> Schedule Session
                                            </a>
                                        </div>

                                        <div class="p-3 bg-light rounded-3 mb-3 small">
                                            <div class="d-flex justify-content-between mb-1">
                                                <span class="text-muted">Skill 1:</span>
                                                <strong class="text-success">${req.offeredSkillName}</strong>
                                            </div>
                                            <div class="d-flex justify-content-between">
                                                <span class="text-muted">Skill 2:</span>
                                                <strong class="text-primary">${req.requestedSkillName}</strong>
                                            </div>
                                        </div>

                                        <div class="mt-auto small text-muted">
                                            <i class="bi bi-info-circle me-1"></i> Accepted on ${req.createdAt}. Ready for learning sessions.
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card-custom p-5 text-center bg-white">
                            <i class="bi bi-calendar-heart text-secondary fs-1 d-block mb-2"></i>
                            <h5 class="fw-bold text-dark">No accepted exchanges yet</h5>
                            <p class="text-muted small mb-0">Once you or your peer accepts an exchange request, you can schedule and conduct learning sessions here.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>

    </div>
</main>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

