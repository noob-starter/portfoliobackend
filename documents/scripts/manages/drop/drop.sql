-- =====================================================
-- DROP ALL TABLES SCRIPT
-- Drops all tables in the correct order (reverse of creation)
-- =====================================================

-- Drop junction tables
DROP TABLE IF EXISTS public.projects_technologies CASCADE;
DROP TABLE IF EXISTS public.experiences_technologies CASCADE;
DROP TABLE IF EXISTS public.profiles_technologies CASCADE;

-- Drop dependent tables
DROP TABLE IF EXISTS public.project_points CASCADE;
DROP TABLE IF EXISTS public.projects CASCADE;
DROP TABLE IF EXISTS public.experience_points CASCADE;
DROP TABLE IF EXISTS public.experiences CASCADE;
DROP TABLE IF EXISTS public.inquires CASCADE;
DROP TABLE IF EXISTS public.achievements CASCADE;
DROP TABLE IF EXISTS public.contacts CASCADE;
DROP TABLE IF EXISTS public.educations CASCADE;
DROP TABLE IF EXISTS public.addresses CASCADE;
DROP TABLE IF EXISTS public.faqs CASCADE;

-- Drop independent tables
DROP TABLE IF EXISTS public.technologies CASCADE;
DROP TABLE IF EXISTS public.profiles CASCADE;

-- Drop functions
DROP FUNCTION IF EXISTS revise_updated_at_timestamp() CASCADE;

