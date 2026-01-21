-- =====================================================
-- TECHNOLOGIES TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================
-- Note: proficiency field accepts enum values: Expert, Advanced, Intermediate, Beginner

CREATE TABLE IF NOT EXISTS public.technologies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    type VARCHAR(16),
    proficiency VARCHAR(16),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO technologies (name, category, type, proficiency, banner, github)
VALUES 
    ('Java', 'Backend', 'Technology', 'Expert', 'https://www.java.com/banner.jpg', 'https://github.com/openjdk'),
    ('Spring Boot', 'Framework', 'Technology', 'Expert', 'https://spring.io/banner.jpg', 'https://github.com/spring-projects/spring-boot'),
    ('React', 'Frontend', 'Interpersonal', 'Intermediate', 'https://reactjs.org/banner.jpg', 'https://github.com/facebook/react'),
    ('PostGressSQL', 'Database', 'Interpersonal', 'Advanced', 'https://postgresql.org/banner.jpg', 'https://github.com/postgres/postgres'),
    ('Docker', 'DevOps', 'Technology', 'Intermediate', 'https://docker.com/banner.jpg', 'https://github.com/docker');

-- =====================================================
-- 3. GET (Retrieve Single Technology by ID)
-- =====================================================

SELECT id, name, category, type, proficiency, banner, github, created_at, updated_at
FROM technologies
WHERE id = 1;

-- =====================================================
-- 4. GET BY NAME (Retrieve Single Technology by Name)
-- =====================================================

SELECT id, name, category, type, proficiency, banner, github, created_at, updated_at
FROM technologies
WHERE name = 'Java';

-- =====================================================
-- 5. GET ALL (Retrieve All Technologies)
-- =====================================================

SELECT id, name, category, type, proficiency, banner, github, created_at, updated_at
FROM technologies
ORDER BY created_at DESC;

-- =====================================================
-- 6. GET BY CATEGORY (Retrieve All Technologies by Category)
-- =====================================================

SELECT id, name, category, type, proficiency, banner, github, created_at, updated_at
FROM technologies
WHERE category = 'Backend'
ORDER BY name ASC;

-- =====================================================
-- 7. UPDATE (Update Technology Information)
-- =====================================================

UPDATE technologies
SET proficiency = 'Expert',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_technologies_name ON technologies(name);
CREATE INDEX idx_technologies_category ON technologies(category);
CREATE INDEX idx_technologies_created_at ON technologies(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_technologies_updated_at
    BEFORE UPDATE ON technologies
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

