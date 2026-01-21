-- =====================================================
-- COMPLETE DATABASE INSERTION SCRIPT
-- Inserts basic required data into the respective tables
-- =====================================================

-- Profile Table
INSERT INTO profiles (fname, lname, sex, bio, banner, intro, contour, url)
VALUES (
           'Pratik',
           'Yawalkar',
           'Male',
           'I hail from Nagpur, India. I''m an AI enthusiast and software engineer passionate about building scalable, reliable, and user-focused applications. My work blends solid engineering principles with a curiosity for how intelligent systems can solve real-world challenges. I enjoy architecting backend systems, designing efficient workflows, and experimenting with machine learning tools that bring products to life. Whether I’m refining an application for performance or exploring new AI capabilities, I’m driven by a desire to create technology that feels both powerful and intuitive. Outside of development, I’m continuously learning—diving into emerging frameworks, improving my engineering practices, and staying up to date with the fast-moving AI landscape. My goal is to build solutions that not only work well at scale but also make a meaningful impact.',
           'https://github.com/noob-starter/portfoliojava/blob/main/urls/myimages/first.png',
           'Welcome! I am a software developer passionate about creating innovative solutions with goal to build my life, from whom I don''t need a vacation from.',
           'Software Engineer,AI Enthusiast,Lifelong Learner,Problem Solver',
           'https://github.com/noob-starter'
       );

-- Technologies Table
INSERT INTO technologies (name, category, type, proficiency, banner, github)
VALUES
    ('Java', 'Backend', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/java.png', 'https://github.com/noob-starter'),
    ('Python', 'Scripting', 'Technology', 'Expert', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/python.png', 'https://github.com/noob-starter'),
    ('C/C++', 'System', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/cpp.png', 'https://github.com/noob-starter'),
    ('SQL', 'Query', 'Technology', 'Advanced', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/sql.png', 'https://github.com/noob-starter'),
    ('JavaScript', 'Scripting', 'Technology', 'Beginner', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/javascript.png', 'https://github.com/noob-starter'),
    ('HTML5', 'Frontend', 'Technology', 'Advanced', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/html.png', 'https://github.com/noob-starter'),
    ('CSS3', 'Styling', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/css.png', 'https://github.com/noob-starter'),
    ('Spring Boot', 'Framework', 'Technology', 'Expert', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/springboot.png', 'https://github.com/noob-starter'),
    ('React', 'Frontend', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/react.png', 'https://github.com/noob-starter'),
    ('Node', 'Backend', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/nodejs.png', 'https://github.com/noob-starter'),
    ('Flask', 'Framework', 'Technology', 'Beginner', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/flask.png', 'https://github.com/noob-starter'),
    ('Kafka', 'Messaging', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/kafka.png', 'https://github.com/noob-starter'),
    ('GRPC', 'Communication', 'Technology', 'Beginner', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/grpc.png', 'https://github.com/noob-starter'),
    ('JUnit', 'Testing', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/junit.png', 'https://github.com/noob-starter'),
    ('Docker', 'Containerization', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/docker.png', 'https://github.com/noob-starter'),
    ('System Design', 'Design', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/systemdesign.png', 'https://github.com/noob-starter'),
    ('AWS', 'Cloud Computing', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/aws.png', 'https://github.com/noob-starter'),
    ('PostGresSQL', 'Database', 'Technology', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/postgress.png', 'https://github.com/noob-starter'),
    ('DynamoDB', 'Database', 'Technology', 'Advanced', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/dynamodb.png', 'https://github.com/noob-starter'),
    ('MongoDB', 'Database', 'Technology', 'Beginner', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/mongodb.png', 'https://github.com/noob-starter'),
    ('Problem Solving', 'Personal', 'Interpersonal', 'Advanced', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/problemsolving.png', 'https://github.com/noob-starter'),
    ('Collaboration', 'Social', 'Interpersonal', 'Beginner', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/collaboration.png', 'https://github.com/noob-starter'),
    ('Adaptability', 'Personal', 'Interpersonal', 'Expert', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/adaptability.png', 'https://github.com/noob-starter'),
    ('Patience', 'Personal', 'Interpersonal', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/patience.png', 'https://github.com/noob-starter'),
    ('Active Listening', 'Personal', 'Interpersonal', 'Intermediate', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/activelistening.png', 'https://github.com/noob-starter'),
    ('Trust Building', 'Social', 'Interpersonal', 'Advanced', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/trustbuilding.png', 'https://github.com/noob-starter');


-- Address Table
INSERT INTO addresses (street, landmark, city, state, country, pincode, type, phone, email, url, profile_id)
VALUES (
           '33, Ingle Layout, Bhamti Ring Road',
           'Near Nageshwar Mandir',
           'Nagpur',
           'Maharashtra',
           'India',
           440022,
           'Home',
           '+91-7016621949',
           'pratikyawalkar71@gmail.com',
           'https://maps.app.goo.gl/kzAK39WxDgD2sUWy9',
           1
       );

-- Education Table
INSERT INTO educations (degree, institution, field, start_date, end_date, percentage, description, url, banner, github, profile_id)
VALUES (
           'Master of Technology',
           'Indian Institute of Technology, Hyderabad',
           'Artificial Intelligence and Machine Learning',
           '2023-07-01',
           '2025-05-31',
           8.90,
           'Focused on artificial intelligence, deep learning, and data science, with a particular emphasis on robotics applications. I developed an innovative trajectory-planning algorithm designed specifically for fruit-picking robots. The algorithm integrates contextual environmental data—such as branch density, fruit location, and obstacle proximity—into the planning process. By doing so, it significantly improves the robot’s ability to navigate complex orchard environments efficiently and safely. I also enhanced traditional path-planning methods, particularly the A algorithm, by embedding environmental cost factors directly into its objective function. This modification allows the system to distinguish and manage both “soft” obstacles (like leaves or flexible branches) and “hard” obstacles (like tree trunks or rigid structures). The result is a more adaptive, context-aware navigation approach that offers better performance than standard A in real-world agricultural settings.',
           'https://iith.ac.in',
           'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/iith.png',
           'https://github.com/noob-starter',
           1
       ),
       (
           'Bachelor of Engineering',
           'Shri Ramdeobaba College of Engineering and Management, Nagpur',
           'Computer Science and Engineering (Honors)',
           '2019-07-01',
           '2023-05-31',
           8.48,
           'Focused on core areas of computer science and engineering. Developed a gender recognition system using voice, applying signal processing, data analytics and machine learning techniques. Extracted key acoustic features and trained classification models to accurately distinguish male and female voices, improving recognition reliability across diverse speech samples.',
           'https://www.rknec.edu/',
           'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/rcoem.png',
           'https://github.com/noob-starter',
           1
       ),
       (
           'Senior Secondary Education (HSC)',
           'Dharampeth M.P. Deo Memorial Science College, Nagpur',
           'Science Stream',
           '2018-07-01',
           '2019-05-31',
           89.85,
           'Completed HSC in Science with Computer Science as an elective, gaining foundational knowledge in programming, algorithms, and computational thinking. Built strong fundamentals in physics and mathematics while developing practical skills in coding, problem-solving, and logical reasoning.',
           'https://www.dharampethscience.com/',
           'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/dharampethscience.png',
           'https://github.com/noob-starter',
           1
       ),
       (
           'Secondary Education (SSC)',
           'Tip Top Convent, Nagpur',
           'General Education',
           '2016-07-01',
           '2017-05-31',
           92.80,
           'Completed SSC with all general subjects, building a solid foundation in core academic areas including mathematics, science, and languages. Developed essential skills in analytical thinking, communication, and overall academic discipline.',
           'https://www.tiptopconvent.com/',
           'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/tiptopconvent.png',
           'https://github.com/noob-starter',
           1
       );

-- Contacts Table
INSERT INTO contacts (platform, url, description, banner, profile_id)
VALUES
    ('LinkedIn', 'https://www.linkedin.com/in/pratik-yawalkar', 'Professional networking and career development', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/linkedin.png', 1),
    ('GitHub', 'https://github.com/noob-starter', 'Open source projects and code repositories', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/github.png', 1),
    ('Location', 'Nagpur, India', 'Open for Remote and Onsite', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/location.png', 1),
    ('Email', 'pratikyawalkar71@gmail.com', 'Open source projects and code repositories', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/email.png', 1),
    ('Working Hours', '', 'Standard 9:30 to 15:30 hours (Flexible)', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/workinghours.png', 1),
    ('Instagram', 'https://www.instagram.com/yawalkar.pratik_370', 'Connect with me personally', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/instagram.png', 1),
    ('Phone', '+91-7016621949', 'Professional networking and career development', 'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/phone.png', 1);

-- Achievements Table
INSERT INTO achievements (name, date_achieved, issuer, description, url, banner, github, profile_id)
VALUES (
    'Conference Paper',
    '2025-07-02',
    'Advances In Robotics',
    'Published research paper titled "Robot Motion Planning by Non-prehensile Obstruction Handling in Cluttered Environment" at Advances in Robotics, IIT Jodhpur under "Planning and navigation in unstructured environments".',
    'https://advancesinrobotics.com/2025/',
    'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/airjodhpur.png',
    'https://github.com/noob-starter',
    1
    ),
    (
    'Technology Exchange visit Japan',
    '2024-12-16',
    'Japan Science and Technology Agency (JST)',
    'Selected out of 200 students of the AI Dept. IITH to visit Ehime University, Matsuyama, Japan under Sakura Science Program managed by Japan Science and Technology Agency.',
    'https://ssp.jst.go.jp/en/',
    'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/jstjapanvisit.png',
    'https://github.com/noob-starter',
    1
    ),
    (
    'Graduate Aptitude Test in Engineering 2023',
    '2023-02-05',
    'GATE 2023 Committee (IIT Kanpur)',
    'Secured Top 0.2% in CS & IT stream with AIR-370.',
    'https://gate.iitk.ac.in/GATE2023/',
    'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/gate2023.png',
    'https://github.com/noob-starter',
    1
    );

-- Inquires Table
INSERT INTO inquires (name, email, message, profile_id)
VALUES 
    ('John Smith', 'john.smith@techcorp.com', 'Hi, I am impressed by your portfolio. We have an exciting full-stack developer position that matches your skills. Would you be interested in discussing this opportunity?', 1),
    ('Vidhi Pandey', 'vidhi.pandey@startup.io', 'Hello! I came across your GitHub projects and I am really impressed with your work on the backend systems. We are building a fintech platform and would love to have a conversation about potential collaboration.', 1),
    ('Michael Chen', 'mchen@consulting.com', 'Your experience with Spring Boot and microservices architecture caught my attention. We have a client project that could benefit from your expertise. Are you available for a consulting engagement?', 1),
    ('Emily Rodriguez', 'emily.rodriguez@agency.com', 'Hi there! We are working on a large-scale e-commerce platform and need someone with your backend development skills. Would you be open to discussing a contract position?', 1),
    ('Ram J', 'ram.j@enterprise.com', 'I noticed your achievements in cloud architecture. Our team is looking for a senior backend engineer to lead our cloud migration project. Interested in learning more?', 1);

-- Experience Table
INSERT INTO experiences (company, position, start_date, end_date, location, url, banner, github, profile_id)
VALUES (
           'Warner Bros Discovery',
           'Software Engineer 1',
           '2025-07-21',
           null,
           'Hyderabad, India',
           'https://wbd.com',
           'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/wbd.png',
           'https://github.com/noob-starter',
           1
       );

-- Experience Points Table
INSERT INTO experience_points (content, experience_id)
VALUES
    ('Architected and developed a multi-microservice fraud prevention platform comprising 3 Spring Boot services handling real-time decision-making with gRPC inter-service communication and ML model integration for predictive fraud scoring.', 1),
    ('Designed and implemented an event-driven decisioning pipeline processing Kafka streams through state machines, with automated dispute evidence generation using Databricks, enabling data-driven fraud mitigation at scale with comprehensive Micrometer-based observability.', 1);

-- Projects Table
INSERT INTO projects (name, start_date, end_date, url, banner, github, profile_id)
VALUES (
    'Trajectory Planning using Contextual Information (AI in Robotics)',
    '2024-07-01',
    '2025-05-30',
    'https://ecommerce-demo.com',
    'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/robot.png',
    'https://github.com/noob-starter',
    1
    ),
    (
       'Financial Fraud Detection and Classification with Synthetic Data Generation',
       '2024-01-01',
       '2024-05-30',
       'https://ecommerce-demo.com',
       'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/fraud.png',
       'https://github.com/noob-starter',
       1
    ),
    (
       'Product Packaging Application',
       '2022-04-01',
       '2022-06-30',
       'https://ecommerce-demo.com',
       'https://github.com/noob-starter/portfoliojava/blob/main/urls/banners/languages/packaging.png',
       'https://github.com/noob-starter',
       1
    );

-- Projects Points Table
INSERT INTO project_points (content, project_id)
VALUES
    ('Developed an innovative trajectory planning algorithm that integrates contextual environmental data to optimize performance (specific to fruit picking), addressing how this integration improves efficiency in navigating obstacles.', 1),
    ('Enhanced Traditional Algorithms (A*) by incorporating environmental cost factors into the objective function, enabling the effective management of both soft and hard obstacles in a novel manner.', 1),
    ('Mentor: Dr. Rekha Raja, Assistant Professor in AI Dept. at IIT Hyderabad.', 1),
    ('Collaboratively Enhanced transactional security using AutoEncoder Model by uncovering underlying patterns utilizing Node2Vec, Graph CNN & SC Tech. along with Synthetic data generation for model upgrades', 2),
    ('The selected model which integrates the Variational Autoencoder with Cost-Sensitive Logistic Regression (Bhansen) significantly outperformed traditional models, achieving a 50% improvement.', 2),
    ('Spearheaded the development of the Dynamic SPAs titled ”IMDB” using Restful Web APIs and Model View Controller Architecture with the primary focus on illuminating Cinematic Highlights.', 3),
    ('Engineered an app that integrates an extensive IMDB movie database, delivering detailed information and enabling user-generated ratings and reviews, empowering over 100 users to create personalized watch-lists & share insights', 3);

-- Profiles_Technologies Table
INSERT INTO profiles_technologies (profile_id, technology_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
	(1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 10),
    (1, 11),
    (1, 12),
    (1, 13),
    (1, 14),
    (1, 15),
    (1, 16),
    (1, 17),
    (1, 18),
    (1, 19),
    (1, 20),
    (1, 21),
    (1, 22),
    (1, 23),
    (1, 24),
    (1, 25),
    (1, 26);

-- Experiences_Technologies Table
INSERT INTO experiences_technologies (experience_id, technology_id)
VALUES
    (1, 1);

-- Projects_Technologies Table
INSERT INTO projects_technologies (project_id, technology_id)
VALUES
    (1, 1);

-- FAQs Table
INSERT INTO faqs (question, answer, profile_id)
VALUES
    ('Are You aware of AI Tools used by industry?','Yes, I''am aware. I commonly use Cursor, Github-copilot, ChaGPT, and GrokAI.', 1),
    ('What technologies do you specialize in?', 'I specialize in Java, Spring Boot, React, Artificial Intelligence, and SQL. I have extensive experience building full-stack web applications with modern frameworks and best practices. Also, I have developed applications containing agents.', 1),
    ('How many years of experience do you have?', 'I have begun my professional journey as software development engineer, working on various microservices under fraud detection category used to block VPNs, Account Sharing, ChargeBacks, and Payment Risk Identification.', 1),
    ('Can I work with you 1-on-1?','Absolutely! I offer one-on-one collaboration for both project development and mentoring. This includes direct communication via video calls, screen sharing sessions, and collaborative coding. Whether you need help with a specific project, want to learn new technologies, or require technical guidance, I''m available for personalized support tailored to your needs.', 1),
    ('What if main issue goes different?','Flexibility is key in software development. If project requirements change or unexpected challenges arise, I adapt accordingly. All projects include regular check-ins and milestone reviews to ensure we''re aligned. If scope changes significantly, I''ll provide updated timelines and cost estimates. My agile approach ensures we can pivot when needed while maintaining project quality and meeting your core objectives.', 1),
    ('Are you available for freelance projects?', 'Yes, I am available for freelance projects. Please feel free to reach out to me through the contact form or via email.', 1);
