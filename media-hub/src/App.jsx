import { useState, useEffect } from 'react';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import ProfilePage from './components/ProfilePage';
import ThemeToggle from './components/ThemeToggle';
import './styles/globals.css';
import './styles/App.css';

export default function App() {
  // Simple client-side routing: 'login' | 'register' | 'profile'
  const [page, setPage] = useState('login');

  useEffect(() => {
    const token = sessionStorage.getItem('token');
    if (token) setPage('profile');
  }, []);

  const handleLoginSuccess = () => setPage('profile');
  const handleLogout = () => {
    sessionStorage.removeItem('token');
    setPage('login');
  };

  return (
    <div className="app">
      <ThemeToggle />

      {page === 'login' && (
        <LoginForm
          onSuccess={handleLoginSuccess}
          onGoRegister={() => setPage('register')}
        />
      )}

      {page === 'register' && (
        <RegisterForm
          onGoLogin={() => setPage('login')}
        />
      )}

      {page === 'profile' && (
        <ProfilePage onLogout={handleLogout} />
      )}
    </div>
  );
}
