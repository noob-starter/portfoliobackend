-- =====================================================
-- DROP ADMIN TABLE SCRIPT
-- =====================================================

-- Drop admin table
DROP TABLE IF EXISTS public.admins CASCADE;

-- Drop functions
DROP FUNCTION IF EXISTS revise_updated_at_timestamp() CASCADE;