-- =====================================================
-- CONTACTS TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
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

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO contacts (platform, url, description, banner, profile_id)
VALUES 
    ('LinkedIn', 'https://www.linkedin.com/in/pratik-yawalkar', 'Professional networking and career development', 'linkedin-banner.jpg', 1),
    ('GitHub', 'https://github.com/pyawalkar', 'Open source projects and code repositories', 'github-banner.jpg', 1),
    ('Twitter', 'https://twitter.com/pyawalkar', 'Social media updates and tech discussions', 'twitter-banner.jpg', 1);

-- =====================================================
-- 3. GET (Retrieve Single Contact by ID)
-- =====================================================

SELECT id, platform, url, description, banner, profile_id, created_at, updated_at
FROM contacts
WHERE id = 1;

-- =====================================================
-- 4. GET ALL (Retrieve All Contacts)
-- =====================================================

SELECT id, platform, url, description, banner, profile_id, created_at, updated_at
FROM contacts
ORDER BY created_at DESC;

-- =====================================================
-- 5. GET BY PROFILE (Retrieve All Contacts for Profile)
-- =====================================================

SELECT id, platform, url, description, banner, profile_id, created_at, updated_at
FROM contacts
WHERE profile_id = 1
ORDER BY created_at DESC;

-- =====================================================
-- 6. UPDATE (Update Contact Information)
-- =====================================================

UPDATE contacts
SET url = 'https://www.linkedin.com/in/pratik-yawalkar-updated',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_contacts_profile_id ON contacts(profile_id);
CREATE INDEX idx_contacts_created_at ON contacts(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_contacts_updated_at
    BEFORE UPDATE ON contacts
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

