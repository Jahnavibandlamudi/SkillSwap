<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Explore Skills &amp; Manage Interests" />
<c:set var="activePage" value="skills" />
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

        <!-- Header & Action Row -->
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
            <div>
                <h3 class="fw-bold text-dark mb-1">Skills Directory</h3>
                <p class="text-muted small mb-0">Browse university skill domains, add teaching capabilities, or choose what you want to learn.</p>
            </div>
            <div class="d-flex gap-2">
                <button type="button" class="btn btn-outline-success btn-sm" data-bs-toggle="modal" data-bs-target="#addTeachingSkillModal">
                    <i class="bi bi-plus-lg me-1"></i> Add Teaching Skill
                </button>
                <button type="button" class="btn btn-outline-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addLearningSkillModal">
                    <i class="bi bi-plus-lg me-1"></i> Add Learning Interest
                </button>
            </div>
        </div>

        <!-- Filter and Search Bar -->
        <div class="card-custom p-3 mb-4 bg-white">
            <form action="${pageContext.request.contextPath}/skills" method="GET" class="row g-2 align-items-center">
                <div class="col-md-5">
                    <div class="input-group">
                        <span class="input-group-text bg-light text-muted border-end-0"><i class="bi bi-search"></i></span>
                        <input type="text" class="form-control border-start-0" name="q" value="${searchQuery}"
                               placeholder="Search skills by name or keyword...">
                    </div>
                </div>
                <div class="col-md-4">
                    <select class="form-select" name="category" onchange="this.form.submit()">
                        <option value="">All Categories</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat}" ${selectedCategory == cat ? 'selected' : ''}>${cat}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3 d-flex gap-2">
                    <button type="submit" class="btn btn-primary flex-fill">Filter</button>
                    <c:if test="${not empty selectedCategory || not empty searchQuery}">
                        <a href="${pageContext.request.contextPath}/skills" class="btn btn-subtle" title="Reset Filters">
                            <i class="bi bi-arrow-counterclockwise"></i>
                        </a>
                    </c:if>
                </div>
            </form>
        </div>

        <!-- Skills Catalog Grid -->
        <div class="row g-4 mb-5">
            <c:choose>
                <c:when test="${not empty skills}">
                    <c:forEach var="sk" items="${skills}">
                        <div class="col-lg-4 col-md-6">
                            <div class="card-custom p-4 h-100 d-flex flex-column bg-white">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <h5 class="fw-bold text-dark mb-0">${sk.skillName}</h5>
                                    <span class="badge bg-light text-secondary border">${sk.category}</span>
                                </div>
                                <p class="small text-muted mb-3 flex-grow-1">${sk.description}</p>
                                
                                <div class="p-2 px-3 bg-light rounded-3 small text-secondary d-flex justify-content-between mb-3">
                                    <span><i class="bi bi-person-check text-success me-1"></i> <strong>${sk.offeredCount}</strong> teaching</span>
                                    <span><i class="bi bi-person-raised-hand text-primary me-1"></i> <strong>${sk.wantedCount}</strong> learning</span>
                                </div>

                                <div class="d-flex gap-2">
                                    <button type="button" class="btn btn-outline-success btn-sm flex-fill"
                                            onclick="openAddModal(${sk.skillId}, '${sk.skillName}', 'OFFER')">
                                        <i class="bi bi-mortarboard me-1"></i> I Can Teach
                                    </button>
                                    <button type="button" class="btn btn-outline-primary btn-sm flex-fill"
                                            onclick="openAddModal(${sk.skillId}, '${sk.skillName}', 'WANT')">
                                        <i class="bi bi-book me-1"></i> I Want to Learn
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-search text-secondary fs-1 d-block mb-2"></i>
                        <h5 class="fw-bold text-dark">No skills match your search criteria</h5>
                        <p class="text-muted small">Try selecting a different category or clearing the search query.</p>
                        <a href="${pageContext.request.contextPath}/skills" class="btn btn-primary btn-sm">Clear Filters</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Availability Schedule Section -->
        <div id="availability" class="card-custom mb-4 bg-white">
            <div class="card-header-clean">
                <div class="d-flex align-items-center gap-2">
                    <i class="bi bi-clock-history text-primary fs-5"></i>
                    <span class="fw-bold">Your Weekly Learning &amp; Teaching Schedule</span>
                </div>
                <button type="button" class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#addAvailabilityModal">
                    <i class="bi bi-plus-lg me-1"></i> Add Availability Slot
                </button>
            </div>
            <div class="p-3">
                <p class="small text-muted mb-3">
                    Setting your available schedule allows SkillSwap's matching engine to calculate schedule compatibility scores (up to 20 points).
                </p>
                <c:choose>
                    <c:when test="${not empty myAvailability}">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle small mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>Day of the Week</th>
                                        <th>Starting Time</th>
                                        <th>Ending Time</th>
                                        <th>Duration</th>
                                        <th class="text-end">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="slot" items="${myAvailability}">
                                        <tr>
                                            <td class="fw-bold text-dark">${slot.dayOfWeek}</td>
                                            <td>${slot.startTime.toString().substring(0, 5)}</td>
                                            <td>${slot.endTime.toString().substring(0, 5)}</td>
                                            <td><span class="badge bg-light text-secondary border">Active Slot</span></td>
                                            <td class="text-end">
                                                <form action="${pageContext.request.contextPath}/skills" method="POST" class="d-inline">
                                                    <input type="hidden" name="action" value="removeAvailability">
                                                    <input type="hidden" name="availabilityId" value="${slot.availabilityId}">
                                                    <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2">
                                                        <i class="bi bi-trash"></i> Remove
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-4 text-muted small">
                            <i class="bi bi-calendar3 fs-3 d-block text-secondary mb-1"></i>
                            No availability schedule configured yet. Click "Add Availability Slot" above to set your free hours.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>
</main>

<!-- Unified Modal to Add Skill with Proficiency -->
<div class="modal fade" id="addSkillModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold" id="addSkillModalTitle">Add Skill</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/skills" method="POST">
                <input type="hidden" name="action" value="addSkill">
                <input type="hidden" id="modalSkillId" name="skillId" value="">
                <input type="hidden" id="modalSkillType" name="skillType" value="OFFER">

                <div class="modal-body">
                    <div class="p-3 bg-light rounded-3 mb-3">
                        <div class="small text-muted text-uppercase fw-bold">Selected Skill</div>
                        <h5 class="fw-bold text-dark mb-0" id="modalSkillName">Skill Name</h5>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="proficiencySelect">Proficiency Level</label>
                        <select class="form-select" id="proficiencySelect" name="proficiency" required>
                            <option value="Beginner">Beginner (Fundamentals &amp; Basics)</option>
                            <option value="Intermediate">Intermediate (Practical Projects &amp; Lab Work)</option>
                            <option value="Advanced">Advanced (Deep Problem Solving &amp; Mastery)</option>
                            <option value="Expert">Expert (Hackathon Winner / Industry Ready)</option>
                        </select>
                        <div class="form-text small text-muted">Be honest with your proficiency for fair peer matchmaking.</div>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="modalSubmitBtn">Save to Profile</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal: Add Teaching Skill from Catalog Dropdown -->
<div class="modal fade" id="addTeachingSkillModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold">Add Teaching Capability</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/skills" method="POST">
                <input type="hidden" name="action" value="addSkill">
                <input type="hidden" name="skillType" value="OFFER">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Select Skill from Catalog</label>
                        <select class="form-select" name="skillId" required>
                            <option value="">Choose Skill...</option>
                            <c:forEach var="s" items="${allCatalogSkills}">
                                <option value="${s.skillId}">${s.skillName} (${s.category})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Your Proficiency</label>
                        <select class="form-select" name="proficiency" required>
                            <option value="Intermediate">Intermediate</option>
                            <option value="Advanced">Advanced</option>
                            <option value="Expert">Expert</option>
                            <option value="Beginner">Beginner</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-success">Add to Teaching Skills</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal: Add Learning Interest from Catalog Dropdown -->
<div class="modal fade" id="addLearningSkillModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold">Add Learning Interest</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/skills" method="POST">
                <input type="hidden" name="action" value="addSkill">
                <input type="hidden" name="skillType" value="WANT">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Select Skill from Catalog</label>
                        <select class="form-select" name="skillId" required>
                            <option value="">Choose Skill...</option>
                            <c:forEach var="s" items="${allCatalogSkills}">
                                <option value="${s.skillId}">${s.skillName} (${s.category})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Current Level</label>
                        <select class="form-select" name="proficiency" required>
                            <option value="Beginner">Beginner (Starting from scratch)</option>
                            <option value="Intermediate">Intermediate (Looking to improve)</option>
                            <option value="Advanced">Advanced (Polishing advanced topics)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add to Learning List</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal: Add Availability Slot -->
<div class="modal fade" id="addAvailabilityModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold">Add Weekly Availability Slot</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/skills" method="POST">
                <input type="hidden" name="action" value="addAvailability">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label" for="daySelect">Day of the Week</label>
                        <select class="form-select" id="daySelect" name="dayOfWeek" required>
                            <option value="Monday">Monday</option>
                            <option value="Tuesday">Tuesday</option>
                            <option value="Wednesday">Wednesday</option>
                            <option value="Thursday">Thursday</option>
                            <option value="Friday">Friday</option>
                            <option value="Saturday">Saturday</option>
                            <option value="Sunday">Sunday</option>
                        </select>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label" for="startSelect">Start Time</label>
                            <input type="time" class="form-control" id="startSelect" name="startTime" value="17:00" required>
                        </div>
                        <div class="col-6">
                            <label class="form-label" for="endSelect">End Time</label>
                            <input type="time" class="form-control" id="endSelect" name="endTime" value="19:00" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Slot</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function openAddModal(skillId, skillName, type) {
    document.getElementById('modalSkillId').value = skillId;
    document.getElementById('modalSkillType').value = type;
    document.getElementById('modalSkillName').textContent = skillName;
    if (type === 'OFFER') {
        document.getElementById('addSkillModalTitle').textContent = 'Add to Teaching Skills';
        document.getElementById('modalSubmitBtn').textContent = 'Add Teaching Skill';
        document.getElementById('modalSubmitBtn').className = 'btn btn-success';
    } else {
        document.getElementById('addSkillModalTitle').textContent = 'Add to Learning Interests';
        document.getElementById('modalSubmitBtn').textContent = 'Add Learning Interest';
        document.getElementById('modalSubmitBtn').className = 'btn btn-primary';
    }
    const modal = new bootstrap.Modal(document.getElementById('addSkillModal'));
    modal.show();
}
</script>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

