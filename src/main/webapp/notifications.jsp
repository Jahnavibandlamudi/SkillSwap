<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Notifications Center" />
<c:set var="activePage" value="notifications" />
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

        <!-- Header -->
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
            <div>
                <h3 class="fw-bold text-dark mb-1">Notifications Center</h3>
                <p class="text-muted small mb-0">Stay informed on skill exchange proposals, scheduled sessions, and virtual credit transfers.</p>
            </div>
            <c:if test="${unreadCount > 0}">
                <form action="${pageContext.request.contextPath}/notifications" method="POST">
                    <input type="hidden" name="action" value="markAllRead">
                    <button type="submit" class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-check2-all me-1"></i> Mark All as Read (${unreadCount})
                    </button>
                </form>
            </c:if>
        </div>

        <!-- Notifications List -->
        <div class="card-custom bg-white">
            <div class="p-3">
                <c:choose>
                    <c:when test="${not empty notifications}">
                        <div class="d-flex flex-column gap-2">
                            <c:forEach var="n" items="${notifications}">
                                <div class="p-3 rounded-3 border d-flex justify-content-between align-items-center gap-3 ${n.read ? 'bg-white' : 'bg-primary-subtle border-primary'}">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="avatar avatar-sm ${n.type == 'WALLET' ? 'bg-warning-subtle text-warning' : (n.type == 'SESSION' ? 'bg-success-subtle text-success' : 'bg-primary-subtle text-primary')}">
                                            <i class="bi ${n.type == 'WALLET' ? 'bi-coin' : (n.type == 'SESSION' ? 'bi-calendar-check' : (n.type == 'REVIEW' ? 'bi-star' : 'bi-bell'))}"></i>
                                        </div>
                                        <div>
                                            <div class="text-dark small ${n.read ? '' : 'fw-bold'}">${n.message}</div>
                                            <div class="text-muted" style="font-size: 0.75rem;">
                                                <i class="bi bi-clock me-1"></i> ${n.createdAt}
                                            </div>
                                        </div>
                                    </div>

                                    <div class="d-flex align-items-center gap-2">
                                        <c:if test="${not empty n.link && n.link != '#'}">
                                            <a href="${pageContext.request.contextPath}/${n.link}" class="btn btn-sm btn-subtle" style="font-size: 0.78rem;">
                                                View &rarr;
                                            </a>
                                        </c:if>
                                        <c:if test="${!n.read}">
                                            <form action="${pageContext.request.contextPath}/notifications" method="POST" class="d-inline">
                                                <input type="hidden" name="action" value="markRead">
                                                <input type="hidden" name="id" value="${n.notificationId}">
                                                <button type="submit" class="btn btn-sm btn-outline-secondary py-0 px-2" title="Mark as Read" style="font-size: 0.75rem;">
                                                    <i class="bi bi-check"></i>
                                                </button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5 text-muted small">
                            <i class="bi bi-bell-slash text-secondary fs-2 d-block mb-2"></i>
                            You have no notifications right now.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

