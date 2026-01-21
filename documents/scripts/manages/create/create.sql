-- =====================================================
-- COMPLETE DATABASE CREATION SCRIPT
-- Creates all tables in the correct order, all triggers, indexes, and foreign keys.
-- =====================================================

-- =====================================================
-- 1. COMMON FUNCTIONS FOR ALL TRIGGERS
-- =====================================================

CREATE OR REPLACE FUNCTION revise_updated_at_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- 2. CREATE PROFILES TABLE (Independent table)
-- =====================================================

CREATE TABLE IF NOT EXISTS public.profiles (
    id BIGSERIAL PRIMARY KEY,
    fname VARCHAR(255) NOT NULL,
    lname VARCHAR(255) NOT NULL,
    sex VARCHAR(64),
    bio TEXT,
    banner VARCHAR(512),
    intro TEXT,
    contour TEXT,
    url VARCHAR(512) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_profiles_fname ON profiles(fname);
CREATE INDEX idx_profiles_lname ON profiles(lname);
CREATE INDEX idx_profiles_created_at ON profiles(created_at);

CREATE TRIGGER update_profiles_updated_at
    BEFORE UPDATE ON profiles
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 3. CREATE TECHNOLOGIES TABLE (Independent table)
-- =====================================================

CREATE TABLE IF NOT EXISTS public.technologies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    type VARCHAR(16),
    proficiency VARCHAR(16),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_technologies_name ON technologies(name);
CREATE INDEX idx_technologies_category ON technologies(category);
CREATE INDEX idx_technologies_created_at ON technologies(created_at);

CREATE TRIGGER update_technologies_updated_at
    BEFORE UPDATE ON technologies
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 4. CREATE ADDRESSES TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.addresses (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255),
    landmark VARCHAR(255),
    city VARCHAR(64),
    state VARCHAR(64),
    country VARCHAR(64),
    pincode INTEGER NOT NULL,
    type VARCHAR(64),
    phone VARCHAR(16),
    email VARCHAR(255),
    url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_addresses_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_addresses_profile_id ON addresses(profile_id);
CREATE INDEX idx_addresses_created_at ON addresses(created_at);
CREATE INDEX idx_addresses_pincode ON addresses(pincode);

CREATE TRIGGER update_addresses_updated_at
    BEFORE UPDATE ON addresses
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 5. CREATE EDUCATIONS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.educations (
    id BIGSERIAL PRIMARY KEY,
    degree VARCHAR(255),
    institution VARCHAR(255),
    field VARCHAR(255),
    start_date DATE,
    end_date DATE,
    percentage NUMERIC(5,2),
    description TEXT,
    url VARCHAR(512),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_educations_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_educations_profile_id ON educations(profile_id);
CREATE INDEX idx_educations_start_date ON educations(start_date);
CREATE INDEX idx_educations_created_at ON educations(created_at);

CREATE TRIGGER update_educations_updated_at
    BEFORE UPDATE ON educations
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 6. CREATE CONTACTS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.contacts (
    id BIGSERIAL PRIMARY KEY,
    platform VARCHAR(255) NOT NULL,
    url VARCHAR(512),
    description TEXT,
    banner VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_contacts_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_contacts_profile_id ON contacts(profile_id);
CREATE INDEX idx_contacts_created_at ON contacts(created_at);

CREATE TRIGGER update_contacts_updated_at
    BEFORE UPDATE ON contacts
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 7. CREATE ACHIEVEMENTS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.achievements (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    date_achieved DATE,
    issuer VARCHAR(255),
    description TEXT,
    url VARCHAR(512),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_achievements_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_achievements_profile_id ON achievements(profile_id);
CREATE INDEX idx_achievements_date_achieved ON achievements(date_achieved);
CREATE INDEX idx_achievements_created_at ON achievements(created_at);

CREATE TRIGGER update_achievements_updated_at
    BEFORE UPDATE ON achievements
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 8. CREATE INQUIRES TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.inquires (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_inquires_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_inquires_profile_id ON inquires(profile_id);
CREATE INDEX idx_inquires_created_at ON inquires(created_at);
CREATE INDEX idx_inquires_email ON inquires(email);

CREATE TRIGGER update_inquires_updated_at
    BEFORE UPDATE ON inquires
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 9. CREATE EXPERIENCES TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.experiences (
    id BIGSERIAL PRIMARY KEY,
    company VARCHAR(255),
    position VARCHAR(255),
    start_date DATE NOT NULL,
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

CREATE INDEX idx_experiences_profile_id ON experiences(profile_id);
CREATE INDEX idx_experiences_start_date ON experiences(start_date);
CREATE INDEX idx_experiences_created_at ON experiences(created_at);

CREATE TRIGGER update_experiences_updated_at
    BEFORE UPDATE ON experiences
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 10. CREATE EXPERIENCE_POINTS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.experience_points (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    experience_id BIGINT NOT NULL,
    CONSTRAINT fk_experience_points_experience FOREIGN KEY (experience_id) REFERENCES experiences(id) ON DELETE CASCADE
);

CREATE INDEX idx_experience_points_experience_id ON experience_points(experience_id);
CREATE INDEX idx_experience_points_created_at ON experience_points(created_at);

CREATE TRIGGER update_experience_points_updated_at
    BEFORE UPDATE ON experience_points
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 11. CREATE PROJECTS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE,
    end_date DATE,
    url VARCHAR(512),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_projects_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_projects_profile_id ON projects(profile_id);
CREATE INDEX idx_projects_start_date ON projects(start_date);
CREATE INDEX idx_projects_created_at ON projects(created_at);

CREATE TRIGGER update_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 12. CREATE PROJECT_POINTS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.project_points (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    project_id BIGINT NOT NULL,
    CONSTRAINT fk_project_points_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE INDEX idx_project_points_project_id ON project_points(project_id);
CREATE INDEX idx_project_points_created_at ON project_points(created_at);

CREATE TRIGGER update_project_points_updated_at
    BEFORE UPDATE ON project_points
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 13. CREATE FAQS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.faqs (
    id BIGSERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_faqs_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_faqs_profile_id ON faqs(profile_id);
CREATE INDEX idx_faqs_created_at ON faqs(created_at);

CREATE TRIGGER update_faqs_updated_at
    BEFORE UPDATE ON faqs
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- =====================================================
-- 14. CREATE JUNCTION TABLES
-- =====================================================

-- Profiles <-> Technologies
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

CREATE INDEX idx_prof_tech_profile_id ON profiles_technologies(profile_id);
CREATE INDEX idx_prof_tech_technology_id ON profiles_technologies(technology_id);

CREATE TRIGGER update_profiles_technologies_updated_at
    BEFORE UPDATE ON profiles_technologies
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- Experiences <-> Technologies
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

CREATE INDEX idx_exp_tech_experience_id ON experiences_technologies(experience_id);
CREATE INDEX idx_exp_tech_technology_id ON experiences_technologies(technology_id);

CREATE TRIGGER update_experiences_technologies_updated_at
    BEFORE UPDATE ON experiences_technologies
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

-- Projects <-> Technologies
CREATE TABLE IF NOT EXISTS public.projects_technologies (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    project_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    CONSTRAINT fk_proj_tech_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_proj_tech_technology FOREIGN KEY (technology_id) REFERENCES technologies(id) ON DELETE CASCADE,
    CONSTRAINT uk_project_technology UNIQUE (project_id, technology_id)
);

CREATE INDEX idx_proj_tech_project_id ON projects_technologies(project_id);
CREATE INDEX idx_proj_tech_technology_id ON projects_technologies(technology_id);

CREATE TRIGGER update_projects_technologies_updated_at
    BEFORE UPDATE ON projects_technologies
    FOR EACH ROW
    EXECUTE FUNCTION revise_updated_at_timestamp();

