import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../components/UI/Button/Button';
import styles from './LandingPage.module.css';

export const LandingPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div>
      <section className={styles.hero}>
        <div className="container">
          <h1 className={styles.title}>Short links, big results</h1>
          <p className={styles.subtitle}>
            A powerful and minimalist URL shortener that gives you full control over your links. 
            Track clicks, customize slugs, and analyze performance.
          </p>
          <div className={styles.ctas}>
            <Button onClick={() => navigate('/register')} className="text-lg" style={{ padding: '0.75rem 1.5rem' }}>
              Get started
            </Button>
            <Button variant="secondary" onClick={() => navigate('/login')} className="text-lg" style={{ padding: '0.75rem 1.5rem' }}>
              Sign in
            </Button>
          </div>
        </div>
      </section>

      <section className={`container ${styles.features}`}>
        <div className={styles.feature}>
          <h3 className={styles.featureTitle}>Custom Slugs</h3>
          <p className={styles.featureDesc}>
            Create memorable, branded links by customizing the back-half of your URLs. Stand out in every click.
          </p>
        </div>
        <div className={styles.feature}>
          <h3 className={styles.featureTitle}>Detailed Analytics</h3>
          <p className={styles.featureDesc}>
            Track performance with comprehensive stats. View total clicks, daily trends, and your top traffic referrers.
          </p>
        </div>
        <div className={styles.feature}>
          <h3 className={styles.featureTitle}>Fast Cache</h3>
          <p className={styles.featureDesc}>
            Powered by an ultra-fast redirector and smart caching layer, ensuring your links load instantly every time.
          </p>
        </div>
      </section>

      <footer className={styles.footer}>
        <p>© {new Date().getFullYear()} Shorty. Minimalist URL Shortener.</p>
      </footer>
    </div>
  );
};
