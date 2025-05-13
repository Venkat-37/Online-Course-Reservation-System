// LearnLoop Main JavaScript - Combined Frontend and Backend Integration
document.addEventListener('DOMContentLoaded', function() {
    // Base API URL
    const API_BASE_URL = 'http://localhost:8080/api';
    
    // ===== Authentication & User Management =====
    function getToken() {
        return localStorage.getItem('authToken');
    }
    
    function setToken(token) {
        localStorage.setItem('authToken', token);
    }
    
    function removeToken() {
        localStorage.removeItem('authToken');
    }
    
    function isLoggedIn() {
        return getToken() !== null;
    }
    
    function getCurrentUserId() {
        // Get user ID from localStorage or fallback to test user
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user).id : 'test_user_123';
    }
    
    // API request helper
    async function apiRequest(endpoint, method = 'GET', data = null) {
        const url = `${API_BASE_URL}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json'
        };
        
        if (isLoggedIn()) {
            headers['Authorization'] = `Bearer ${getToken()}`;
        }
        
        const options = {
            method,
            headers
        };
        
        if (data) {
            options.body = JSON.stringify(data);
        }
        
        try {
            const response = await fetch(url, options);
            
            if (response.status === 401) {
                // Unauthorized - token expired or invalid
                removeToken();
                window.location.href = 'login.html';
                return null;
            }
            
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'API request failed');
            }
            
            if (response.status === 204) {
                return null; // No content
            }
            
            return await response.json();
        } catch (error) {
            console.error('API Request Error:', error);
            throw error;
        }
    }
    
    // Authentication functions
    async function login(email, password) {
        try {
            const data = await apiRequest('/auth/login', 'POST', { email, password });
            if (data && data.token) {
                setToken(data.token);
                localStorage.setItem('user', JSON.stringify({
                    id: data.id,
                    email: data.email,
                    fullName: data.fullName,
                    userType: data.userType,
                    profileCompletionPercentage: data.profileCompletionPercentage
                }));
                return true;
            }
            return false;
        } catch (error) {
            console.error('Login failed:', error);
            return false;
        }
    }
    
    async function register(userData) {
        try {
            const data = await apiRequest('/auth/register', 'POST', userData);
            return data;
        } catch (error) {
            console.error('Registration failed:', error);
            throw error;
        }
    }
    
    function logout() {
        removeToken();
        localStorage.removeItem('user');
        window.location.href = 'index.html';
    }

    // ===== UI Initialization & Effects =====
    
    // Header scroll effect
    window.addEventListener('scroll', function() {
        const header = document.querySelector('header');
        if (header) {
            if (window.scrollY > 50) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        }
    });
    
    // Scroll reveal animation
    const revealElements = document.querySelectorAll('.reveal');
    
    function checkReveal() {
        for (let i = 0; i < revealElements.length; i++) {
            const windowHeight = window.innerHeight;
            const elementTop = revealElements[i].getBoundingClientRect().top;
            const elementVisible = 150;
            
            if (elementTop < windowHeight - elementVisible) {
                revealElements[i].classList.add('active');
            }
        }
    }
    window.addEventListener('scroll', checkReveal);
    checkReveal(); // Check on initial load
    
    // Add floating animation to specified elements
    const floatingElements = document.querySelectorAll('.float-animation');
    floatingElements.forEach(element => {
        element.classList.add('floating');
    });
    
    // Smooth scrolling for navigation links
    document.querySelectorAll('nav a, .cta-buttons a, .footer-col ul li a').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            const href = this.getAttribute('href');
            if (href && href.startsWith('#')) {
                e.preventDefault();
                
                const targetId = href.substring(1);
                const targetElement = document.getElementById(targetId);
                
                if (targetElement) {
                    window.scrollTo({
                        top: targetElement.offsetTop - 80,
                        behavior: 'smooth'
                    });
                }
            }
        });
    });
    
    // ===== Course Management =====
    
    // Course filtering functionality
    const filterBtns = document.querySelectorAll('.filter-btn');
    const courseCards = document.querySelectorAll('.course-card');
    
    filterBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // Remove active class from all buttons
            filterBtns.forEach(btn => btn.classList.remove('active'));
            // Add active class to clicked button
            this.classList.add('active');
            
            const filterValue = this.getAttribute('data-filter');
            
            // Show/hide courses based on filter with animation
            courseCards.forEach(card => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                
                setTimeout(() => {
                    if (filterValue === 'all' || card.getAttribute('data-category') === filterValue) {
                        card.style.display = 'block';
                        setTimeout(() => {
                            card.style.opacity = '1';
                            card.style.transform = 'translateY(0)';
                        }, 50);
                    } else {
                        card.style.display = 'none';
                    }
                }, 300);
            });
        });
    });
    
    async function getFeaturedCourses() {
        try {
            return await apiRequest('/courses/public/featured');
        } catch (error) {
            console.error('Failed to fetch featured courses:', error);
            return [];
        }
    }
    
    async function getCoursesByCategory(category, page = 0, size = 10) {
        try {
            return await apiRequest(`/courses/public/category/${category}?page=${page}&size=${size}`);
        } catch (error) {
            console.error('Failed to fetch courses by category:', error);
            return { content: [] };
        }
    }
    
    async function enrollInCourse(courseId) {
        try {
            return await apiRequest(`/enrollments/${courseId}`, 'POST');
        } catch (error) {
            console.error('Failed to enroll in course:', error);
            throw error;
        }
    }
    
    async function getUserEnrollments() {
        try {
            return await apiRequest('/enrollments');
        } catch (error) {
            console.error('Failed to fetch user enrollments:', error);
            return [];
        }
    }
    
    async function loadFeaturedCourses() {
        try {
            const courses = await getFeaturedCourses();
            
            if (courses && courses.length > 0) {
                // Currently we're just using the existing static course cards
                // But you could dynamically create cards based on the data
                console.log('Featured courses loaded:', courses);
            }
        } catch (error) {
            console.error('Error loading courses:', error);
        }
    }

    // ===== Profile Management =====
    
    // Profile functions
    async function getUserProfile() {
        try {
            return await apiRequest('/users/profile');
        } catch (error) {
            console.error('Failed to fetch user profile:', error);
            return null;
        }
    }
    
    async function updateUserProfile(profileData) {
        try {
            return await apiRequest('/users/profile', 'PUT', profileData);
        } catch (error) {
            console.error('Failed to update user profile:', error);
            throw error;
        }
    }
    
    async function uploadProfilePhoto(fileInput) {
        if (!fileInput.files || fileInput.files.length === 0) {
            throw new Error('No file selected');
        }
        
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);
        
        const url = `${API_BASE_URL}/files/profile-photo`;
        const headers = {};
        
        if (isLoggedIn()) {
            headers['Authorization'] = `Bearer ${getToken()}`;
        }
        
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers,
                body: formData
            });
            
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Upload failed');
            }
            
            return await response.json();
        } catch (error) {
            console.error('File upload error:', error);
            throw error;
        }
    }
    
    function fetchProfileData() {
        const userId = getCurrentUserId();
        
        // Use the API request helper
        apiRequest(`/users/profile`)
            .then(data => {
                if (data) {
                    populateProfileForm(data);
                    updateProfileCompletionUI(data.profileCompletionPercentage);
                } else {
                    // New user, just initialize the UI
                    updateProfileCompletionUI(0);
                }
            })
            .catch(error => {
                console.error('Error fetching profile data:', error);
                showNotification('Could not load your profile data', 'error');
            });
    }
    
    function populateProfileForm(profileData) {
        const form = document.getElementById('profile-form');
        
        if (form) {
            // Set text/select field values
            for (const key in profileData) {
                const input = form.elements[key];
                if (input && key !== 'interests') {
                    input.value = profileData[key];
                }
            }
            
            // Handle interests separately (for tags UI)
            if (profileData.interests && Array.isArray(profileData.interests)) {
                document.getElementById('interests').value = profileData.interests.join(', ');
                
                // Select matching interest tags
                document.querySelectorAll('.interest-tag').forEach(tag => {
                    if (profileData.interests.includes(tag.getAttribute('data-value'))) {
                        tag.classList.add('selected');
                    }
                });
            }
            
            // If profile has an avatar
            if (profileData.avatarUrl) {
                document.querySelector('.profile-avatar').src = profileData.avatarUrl;
            }
        }
    }
    
    function saveProfile() {
        const form = document.getElementById('profile-form');
        if (!form) return;
        
        const profileData = {
            fullName: document.getElementById('fullName').value,
            phone: document.getElementById('phone').value,
            dateOfBirth: document.getElementById('dob').value || null,
            education: document.getElementById('education').value,
            profession: document.getElementById('profession').value,
            interests: document.getElementById('interests').value.split(',').map(i => i.trim()).filter(i => i),
            bio: document.getElementById('bio').value,
            linkedinProfile: document.getElementById('linkedin').value,
            githubProfile: document.getElementById('github').value,
            personalWebsite: document.getElementById('website').value,
            location: document.getElementById('location').value
        };
        
        // Using the new API method
        updateUserProfile(profileData)
            .then(data => {
                if (data) {
                    updateProfileCompletionUI(data.profileCompletionPercentage);
                    showNotification('Profile updated successfully!', 'success');
                }
            })
            .catch(error => {
                console.error('Error saving profile:', error);
                showNotification('Error saving profile. Please try again.', 'error');
            });
    }
    
    function updateProfileCompletionUI(percentage) {
        // Update progress bar
        const progressBar = document.getElementById('completion-progress-bar');
        if (progressBar) {
            progressBar.style.width = percentage + '%';
        }
        
        // Update percentage text
        const percentageText = document.getElementById('profile-completion-percentage');
        if (percentageText) {
            percentageText.textContent = percentage + '%';
        }
        
        // Update profile circle in navbar
        updateProfileCircle(percentage);
    }
    
    function updateProfileCircle(percentage) {
        const profileIcon = document.querySelector('nav a[href="#Profile"] i');
        if (profileIcon) {
            // Remove existing circle if any
            const existingCircle = profileIcon.parentElement.querySelector('.completion-circle');
            if (existingCircle) {
                profileIcon.parentElement.removeChild(existingCircle);
            }
            
            // Create circle with percentage
            const circle = document.createElement('div');
            circle.className = 'completion-circle';
            
            if (percentage < 50) {
                circle.classList.add('low');
            } else if (percentage < 80) {
                circle.classList.add('medium');
            } else {
                circle.classList.add('high');
            }
            
            circle.innerHTML = `<span>${percentage}%</span>`;
            profileIcon.parentElement.appendChild(circle);
        }
    }
    
    function updateInterestsField() {
        const selectedTags = document.querySelectorAll('.interest-tag.selected');
        const interests = Array.from(selectedTags).map(tag => tag.getAttribute('data-value'));
        const interestsField = document.getElementById('interests');
        if (interestsField) {
            interestsField.value = interests.join(', ');
        }
    }
    
    // ===== UI Helpers =====
    
    // Notification function with multiple types
    function showNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        // Fade in
        setTimeout(() => {
            notification.style.opacity = '1';
            notification.style.transform = 'translateY(0)';
        }, 10);
        
        // Fade out and remove
        setTimeout(() => {
            notification.style.opacity = '0';
            notification.style.transform = 'translateY(-20px)';
            
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 500);
        }, 4000);
    }
    
    // Initialize UI based on login status
    function initUI() {
        const loggedIn = isLoggedIn();
        const loginButton = document.querySelector('.login-btn');
        
        if (loggedIn) {
            const user = JSON.parse(localStorage.getItem('user'));
            
            if (loginButton) {
                loginButton.innerHTML = `<i class="fas fa-user"></i> ${user.fullName}`;
                loginButton.href = '#Profile';
            }
            
            // Load profile data if on profile page
            if (window.location.hash === '#Profile' || document.getElementById('profile-form')) {
                fetchProfileData();
            }
        } else {
            if (loginButton) {
                loginButton.innerHTML = '<i class="fas fa-sign-in-alt"></i> Login/Signup';
                loginButton.href = 'login.html';
            }
        }
        
        // Load courses for the course section
        if (document.getElementById('Course')) {
            loadFeaturedCourses();
        }
        
        // Setup interest tags
        document.querySelectorAll('.interest-tag').forEach(tag => {
            tag.addEventListener('click', function() {
                this.classList.toggle('selected');
                updateInterestsField();
            });
        });
        
        // Setup profile form submission
        const profileForm = document.getElementById('profile-form');
        if (profileForm) {
            profileForm.addEventListener('submit', function(e) {
                e.preventDefault();
                saveProfile();
            });
        }
        
        // Setup photo upload button
        const uploadPhotoBtn = document.getElementById('upload-photo-btn');
        if (uploadPhotoBtn) {
            uploadPhotoBtn.addEventListener('click', function() {
                if (isLoggedIn()) {
                    const fileInput = document.createElement('input');
                    fileInput.type = 'file';
                    fileInput.accept = 'image/*';
                    fileInput.onchange = async function() {
                        try {
                            const result = await uploadProfilePhoto(fileInput);
                            if (result && result.fileUrl) {
                                const profileAvatar = document.querySelector('.profile-avatar');
                                if (profileAvatar) {
                                    profileAvatar.src = result.fileUrl;
                                }
                                fetchProfileData(); // Refresh profile data
                            }
                        } catch (error) {
                            showNotification('Failed to upload photo: ' + error.message, 'error');
                        }
                    };
                    fileInput.click();
                } else {
                    showNotification('This feature will be implemented soon!', 'info');
                }
            });
        }
    }
    
    // Add required CSS
    const style = document.createElement('style');
    style.textContent = `
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 25px;
            border-radius: 5px;
            color: white;
            font-weight: 600;
            z-index: 10000;
            opacity: 0;
            transform: translateY(-20px);
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        .notification.success {
            background: var(--accent, #4CAF50);
        }
        .notification.error {
            background: var(--secondary, #f44336);
        }
        .notification.info {
            background: var(--primary, #2196F3);
        }
        .course-card {
            transition: opacity 0.3s ease, transform 0.3s ease;
        }
        .floating {
            animation: float 3s ease-in-out infinite;
        }
        @keyframes float {
            0% { transform: translateY(0); }
            50% { transform: translateY(-10px); }
            100% { transform: translateY(0); }
        }
        
        /* Profile Completion Circle */
        .completion-circle {
            position: absolute;
            top: -5px;
            right: -15px;
            width: 24px;
            height: 24px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 10px;
            font-weight: 700;
            color: white;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }
        .completion-circle.low {
            background-color: #ff6b6b;
        }
        .completion-circle.medium {
            background-color: #ffc107;
        }
        .completion-circle.high {
            background-color: #00c9a7;
        }
    `;
    document.head.appendChild(style);
    
    // Initialize the application
    initUI();
    
    // Export functions for use in other scripts
    window.learnLoopApp = {
        login,
        register,
        logout,
        getUserProfile,
        updateUserProfile,
        uploadProfilePhoto,
        getFeaturedCourses,
        getCoursesByCategory,
        enrollInCourse,
        getUserEnrollments,
        isLoggedIn,
        showNotification
    };
});