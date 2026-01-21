-- =====================================================
-- PROFILES_TECHNOLOGIES JUNCTION TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.profiles_technologies (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    CONSTRAINT fk_prof_tech_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_prof_tech_technology FOREIGN KEY (technology_id) REFERENCES technologies(id) ON DELETE CASCADE,
    CONSTRAINT uk_profile_technology UNIQUE (profile_id, technology_id)
);

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO profiles_technologies (profile_id, technology_id)
VALUES 
    (1, 1),  -- Pratik knows Java
    (1, 2),  -- Pratik knows Spring Boot
    (1, 3),  -- Pratik knows React
    (1, 4);  -- Pratik knows PostgreSQL

-- =====================================================
-- 3. GET ALL TECHNOLOGIES FOR A PROFILE
-- =====================================================

SELECT t.id, t.name, t.category, t.proficiency
FROM technologies t
JOIN profiles_technologies pt ON t.id = pt.technology_id
WHERE pt.profile_id = 1;

-- =====================================================
-- 4. GET ALL PROFILES FOR A TECHNOLOGY
-- =====================================================

SELECT p.id, p.fname, p.lname, p.sex, p.bio, p.url
FROM profiles p
JOIN profiles_technologies pt ON p.id = pt.profile_id
WHERE pt.technology_id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_prof_tech_profile_id ON profiles_technologies(profile_id);
CREATE INDEX idx_prof_tech_technology_id ON profiles_technologies(technology_id);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_profiles_technologies_updated_at
    BEFORE UPDATE ON profiles_technologies
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

