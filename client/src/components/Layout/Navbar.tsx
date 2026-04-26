import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../store/useAuthStore';
import { Button } from '../UI/Button/Button';
import styles from './Navbar.module.css';

export const Navbar: React.FC = () => {
  const { isAuthenticated, logout } = useAuthStore();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <nav className={styles.navbar}>
      <div className={`container ${styles.content}`}>
        <Link to={isAuthenticated ? '/dashboard' : '/'} className={styles.logo}>
          Shorty
        </Link>
        <div className={styles.links}>
          {isAuthenticated ? (
            <>
              <Link to="/dashboard" className={styles.link}>My Links</Link>
              <Button variant="secondary" onClick={handleLogout}>Logout</Button>
            </>
          ) : (
            <>
              <Link to="/login" className={styles.link}>Sign in</Link>
              <Button onClick={() => navigate('/register')}>Get started</Button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};
