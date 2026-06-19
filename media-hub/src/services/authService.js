// Auth Service - aligned with Spring Boot backend

const API_BASE = 'http://localhost:8080';

async function sha256(message) {
  const msgBuffer = new TextEncoder().encode(message);
  const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  return hashArray.map((b) => b.toString(16).padStart(2, '0')).join('');
}

function getToken() {
  return sessionStorage.getItem('token');
}

function authHeaders() {
  const token = getToken();
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
}

export const authService = {
  /**
   * POST /auth/register
   * Body: { firstName, lastName, email, password }
   */
  registerUser: async ({ firstName, lastName, email, password }) => {
    const response = await fetch(`${API_BASE}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ firstName, lastName, email, password }),
    });

    if (response.status === 200 || response.status === 201) {
      return { success: true };
    }
    if (response.status === 409) {
      throw new Error('E-Mail bereits registriert');
    }
    throw new Error(`HTTP ${response.status}`);
  },

  /**
   * POST /auth/login
   * Body: { username, passwordHash } — backend expects SHA-256 hash
   */
  loginUser: async (username, passwordHash) => {
    const response = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, passwordHash }),
    });

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`);
    }

    const data = await response.json(); // { token: "..." }
    return { success: true, token: data.token };
  },

  logout: () => {
    sessionStorage.removeItem('token');
  },

  isLoggedIn: () => !!getToken(),

  validateEmail: (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email),
};

export const profileService = {
  /**
   * GET /api/me/profile
   */
  getProfile: async () => {
    const response = await fetch(`${API_BASE}/api/me/profile`, {
      headers: authHeaders(),
    });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.json();
  },

  /**
   * PUT /api/me/profile
   * Body: UserProfile { firstName, lastName, address, profileImgUrl }
   */
  updateProfile: async (profileData) => {
    const response = await fetch(`${API_BASE}/api/me/profile`, {
      method: 'PUT',
      headers: authHeaders(),
      body: JSON.stringify(profileData),
    });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.json();
  },
};
