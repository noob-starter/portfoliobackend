-- =====================================================
-- EXPERIENCES TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.experiences (
    id BIGSERIAL PRIMARY KEY,
    company VARCHAR(255),
    position VARCHAR(255),
    start_date DATE,
    end_date DATE,
    location VARCHAR(255),
    url VARCHAR(512),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_experiences_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. CREATE EXPERIENCE_POINTS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.experience_points (
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    experience_id BIGINT NOT NULL,
    CONSTRAINT fk_experience_points_experience FOREIGN KEY (experience_id) REFERENCES experiences(id) ON DELETE CASCADE
);

-- =====================================================
-- 3. CREATE JUNCTION TABLE (experiences_technologies)
-- =====================================================

CREATE TABLE IF NOT EXISTS public.experiences_technologies (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    experience_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    CONSTRAINT fk_exp_tech_experience FOREIGN KEY (experience_id) REFERENCES experiences(id) ON DELETE CASCADE,
    CONSTRAINT fk_exp_tech_technology FOREIGN KEY (technology_id) REFERENCES technologies(id) ON DELETE CASCADE,
    CONSTRAINT uk_experience_technology UNIQUE (experience_id, technology_id)
);

-- =====================================================
-- 4. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO experiences (company, position, start_date, end_date, location, url, banner, github, profile_id)
VALUES (
    'Tech Solutions Inc',
    'Senior Software Engineer',
    '2022-01-01',
    '2023-12-31',
    'Mumbai, India',
    'https://techsolutions.com',
    'https://techsolutions.com/banner.jpg',
    'https://github.com/techsolutions',
    1
);

INSERT INTO experience_points (content, experience_id)
VALUES 
    ('Developed microservices architecture using Spring Boot', 1),
    ('Implemented CI/CD pipelines using Jenkins and Docker', 1),
    ('Led a team of 5 developers', 1);

INSERT INTO experiences_technologies (experience_id, technology_id)
VALUES 
    (1, 1),  -- Java
    (1, 2);  -- Spring Boot

-- =====================================================
-- 5. GET (Retrieve Single Experience by ID with Details)
-- =====================================================

SELECT e.id, e.company, e.position, e.start_date, e.end_date, e.location, e.url, e.banner, e.github, e.profile_id, e.created_at, e.updated_at
FROM experiences e
WHERE e.id = 1;

-- Get experience points for an experience
SELECT ep.id, ep.content, ep.experience_id, ep.created_at, ep.updated_at
FROM experience_points ep
WHERE ep.experience_id = 1
ORDER BY ep.created_at ASC;

-- Get technologies for an experience
SELECT t.id, t.name, t.category, t.proficiency
FROM technologies t
JOIN experiences_technologies et ON t.id = et.technology_id
WHERE et.experience_id = 1;

-- =====================================================
-- 6. GET ALL (Retrieve All Experiences)
-- =====================================================

SELECT id, company, position, start_date, end_date, location, url, banner, github, profile_id, created_at, updated_at
FROM experiences
ORDER BY created_at DESC;

-- =====================================================
-- 7. GET BY PROFILE (Retrieve All Experiences for Profile)
-- =====================================================

SELECT id, company, position, start_date, end_date, location, url, banner, github, profile_id, created_at, updated_at
FROM experiences
WHERE profile_id = 1
ORDER BY start_date DESC;

-- =====================================================
-- 8. UPDATE (Update Experience Information)
-- =====================================================

UPDATE experiences
SET position = 'Lead Software Engineer',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_experiences_profile_id ON experiences(profile_id);
CREATE INDEX idx_experiences_start_date ON experiences(start_date);
CREATE INDEX idx_experiences_created_at ON experiences(created_at);

CREATE INDEX idx_experience_points_experience_id ON experience_points(experience_id);
CREATE INDEX idx_experience_points_created_at ON experience_points(created_at);

CREATE INDEX idx_exp_tech_experience_id ON experiences_technologies(experience_id);
CREATE INDEX idx_exp_tech_technology_id ON experiences_technologies(technology_id);

-- =====================================================
-- TRIGGERS FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_experiences_updated_at
    BEFORE UPDATE ON experiences
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

CREATE TRIGGER update_experience_points_updated_at
    BEFORE UPDATE ON experience_points
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

CREATE TRIGGER update_experiences_technologies_updated_at
    BEFORE UPDATE ON experiences_technologies
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

