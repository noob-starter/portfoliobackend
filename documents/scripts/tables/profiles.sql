-- =====================================================
-- PROFILES TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
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

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO profiles (fname, lname, sex, bio, banner, intro, contour, url)
VALUES (
    'Pratik',
    'Yawalkar',
    'Male',
    'AI enthusiast with background in software engineering, passionate about building scalable applications.',
    'https://example.com/banner.jpg',
    'Welcome to my portfolio! I am a software developer passionate about creating innovative solutions.',
    'With extensive experience in full-stack development, I specialize in building scalable and efficient applications.',
    'https://www.google.com/imgres?q=images&imgurl=https%3A%2F%2Fthumbs.dreamstime.com%2Fb%2Fbeautiful-rain-forest-ang-ka-nature-trail-doi-inthanon-national-park-thailand-36703721.jpg&imgrefurl=https%3A%2F%2Fwww.dreamstime.com%2Fphotos-images%2Fka.html&docid=zxfWKVhGGINY-M&tbnid=MGOijTWB5HZCGM&vet=12ahUKEwjSsfyR6pyQAxUTTkEAHW8nNYwQM3oECCIQAA..i&w=800&h=534&hcb=2&itg=1&ved=2ahUKEwjSsfyR6pyQAxUTTkEAHW8nNYwQM3oECCIQAA'
);


-- =====================================================
-- 3. GET (Retrieve Single Profile by ID)
-- =====================================================

SELECT id, fname, lname, sex, bio, banner, intro, contour, url
FROM profiles
WHERE id = 1;

SELECT id, fname, lname, sex, bio, banner, intro, contour, url, created_at, updated_at
FROM profiles
WHERE fname = 'Pratik' AND lname = 'Yawalkar';

-- =====================================================
-- 4. GET ALL (Retrieve All Profiles)
-- =====================================================

SELECT id, fname, lname, sex, bio, banner, intro, contour, url, created_at, updated_at
FROM profiles
ORDER BY fname ASC, lname ASC;

-- =====================================================
-- 5. UPDATE (Update Profile Information)
-- =====================================================

UPDATE profiles
SET bio = 'Full-stack developer with 6 months of experience.',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;


-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_profiles_fname ON profiles(fname);
CREATE INDEX idx_profiles_lname ON profiles(lname);
CREATE INDEX idx_profiles_created_at ON profiles(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_profiles_updated_at
    BEFORE UPDATE ON profiles
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();
