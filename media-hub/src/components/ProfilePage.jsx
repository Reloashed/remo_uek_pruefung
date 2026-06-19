import { useState, useEffect } from 'react';
import { profileService, authService } from '../services/authService';
import '../styles/ProfilePage.css';

const STATUS_LABELS = {
  VERIFIED: { label: 'Verifiziert', cls: 'status-verified' },
  UNVERIFIED: { label: 'Nicht verifiziert', cls: 'status-unverified' },
  LOCKED: { label: 'Gesperrt', cls: 'status-locked' },
};

export default function ProfilePage({ onLogout }) {
  const [profile, setProfile] = useState(null);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    address: '',
    profileImgUrl: '',
  });
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    setIsLoading(true);
    setError('');
    try {
      const data = await profileService.getProfile();
      setProfile(data);
      setFormData({
        firstName: data.firstName || '',
        lastName: data.lastName || '',
        address: data.address || '',
        profileImgUrl: data.profileImgUrl || '',
      });
    } catch (err) {
      if (err.message.includes('401')) {
        // Token expired
        authService.logout();
        onLogout?.();
      } else {
        setError('Profil konnte nicht geladen werden.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setIsSaving(true);
    setError('');
    try {
      const updated = await profileService.updateProfile(formData);
      setProfile(updated);
      setIsEditing(false);
      setSuccessMessage('Profil erfolgreich gespeichert!');
      setTimeout(() => setSuccessMessage(''), 4000);
    } catch (err) {
      setError('Speichern fehlgeschlagen. Bitte versuchen Sie es erneut.');
    } finally {
      setIsSaving(false);
    }
  };

  const handleCancel = () => {
    setFormData({
      firstName: profile?.firstName || '',
      lastName: profile?.lastName || '',
      address: profile?.address || '',
      profileImgUrl: profile?.profileImgUrl || '',
    });
    setIsEditing(false);
    setError('');
  };

  const handleLogout = () => {
    authService.logout();
    onLogout?.();
  };

  const statusInfo = STATUS_LABELS[profile?.profileStatus] || STATUS_LABELS.UNVERIFIED;

  if (isLoading) {
    return (
      <div className="profile-page">
        <div className="profile-loading">
          <div className="loading-spinner" />
          <p>Profil wird geladen…</p>
        </div>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <div className="profile-card">
        {/* Header */}
        <div className="profile-header">
          <div className="profile-avatar">
            {formData.profileImgUrl ? (
              <img src={formData.profileImgUrl} alt="Profilbild" className="avatar-img" />
            ) : (
              <div className="avatar-placeholder">
                {(profile?.firstName?.[0] || '?').toUpperCase()}
                {(profile?.lastName?.[0] || '').toUpperCase()}
              </div>
            )}
          </div>
          <div className="profile-header-info">
            <h1 className="profile-name">
              {profile?.firstName || '–'} {profile?.lastName || ''}
            </h1>
            <span className={`profile-status ${statusInfo.cls}`}>{statusInfo.label}</span>
          </div>
          <button className="logout-button" onClick={handleLogout} title="Abmelden">
            Abmelden
          </button>
        </div>

        {successMessage && <div className="success-message">{successMessage}</div>}
        {error && <div className="error-message">{error}</div>}

        {/* Profile details / edit form */}
        {isEditing ? (
          <form onSubmit={handleSave} className="profile-form">
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="firstName">Vorname</label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  placeholder="Max"
                />
              </div>
              <div className="form-group">
                <label htmlFor="lastName">Nachname</label>
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  placeholder="Mustermann"
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="address">Adresse</label>
              <input
                type="text"
                id="address"
                name="address"
                value={formData.address}
                onChange={handleChange}
                placeholder="Musterstraße 1, 12345 Musterstadt"
              />
            </div>

            <div className="form-group">
              <label htmlFor="profileImgUrl">Profilbild-URL</label>
              <input
                type="url"
                id="profileImgUrl"
                name="profileImgUrl"
                value={formData.profileImgUrl}
                onChange={handleChange}
                placeholder="https://example.com/bild.jpg"
              />
            </div>

            <div className="form-actions">
              <button type="submit" className="submit-button" disabled={isSaving}>
                {isSaving ? 'Wird gespeichert…' : 'Speichern'}
              </button>
              <button type="button" className="cancel-button" onClick={handleCancel}>
                Abbrechen
              </button>
            </div>
          </form>
        ) : (
          <div className="profile-details">
            <div className="detail-row">
              <span className="detail-label">Vorname</span>
              <span className="detail-value">{profile?.firstName || '–'}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Nachname</span>
              <span className="detail-value">{profile?.lastName || '–'}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Adresse</span>
              <span className="detail-value">{profile?.address || '–'}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Status</span>
              <span className={`detail-value profile-status ${statusInfo.cls}`}>
                {statusInfo.label}
              </span>
            </div>

            <button className="submit-button edit-button" onClick={() => setIsEditing(true)}>
              Profil bearbeiten
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
