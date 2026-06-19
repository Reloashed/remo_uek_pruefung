import { useState } from 'react';
import { authService } from '../services/authService';
import '../styles/LoginForm.css';

export default function LoginForm({ onSuccess, onGoRegister }) {
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.username.trim()) newErrors.username = 'Benutzername ist erforderlich';
    if (!formData.password) newErrors.password = 'Passwort ist erforderlich';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setIsLoading(true);
    try {
      const response = await authService.loginUser(formData.username, formData.password);
      if (response.success && response.token) {
        sessionStorage.setItem('token', response.token);
        onSuccess?.();
      } else {
        throw new Error('Kein Token erhalten');
      }
    } catch (error) {
      setErrors({ submit: 'Anmeldung fehlgeschlagen. Bitte Benutzername und Passwort prüfen.' });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-form-container">
      <div className="login-form-card">
        <h1 className="login-title">Media-Hub Anmeldung</h1>
        <p className="login-subtitle">Melden Sie sich mit Ihrem Konto an</p>

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username">Benutzername</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="max.mustermann"
              className={errors.username ? 'input-error' : ''}
              autoComplete="username"
            />
            {errors.username && <span className="error-text">{errors.username}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="password">Passwort</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="••••••••"
              className={errors.password ? 'input-error' : ''}
              autoComplete="current-password"
            />
            {errors.password && <span className="error-text">{errors.password}</span>}
          </div>

          {errors.submit && <div className="error-message">{errors.submit}</div>}

          <button type="submit" className="submit-button" disabled={isLoading}>
            {isLoading ? 'Wird angemeldet...' : 'Anmelden'}
          </button>
        </form>

        <p className="register-link">
          Noch kein Konto?{' '}
          <button className="link-button" onClick={onGoRegister}>
            Hier registrieren
          </button>
        </p>
      </div>
    </div>
  );
}
