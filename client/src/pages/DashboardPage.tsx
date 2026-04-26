import React, { useEffect, useState } from 'react';
import { useUrlStore } from '../store/useUrlStore';
import { Input } from '../components/UI/Input/Input';
import { Button } from '../components/UI/Button/Button';
import { ErrorMessage } from '../components/UI/ErrorMessage/ErrorMessage';
import { Info } from 'lucide-react';
import { UrlCard } from '../components/Dashboard/UrlCard';
import { StatsPanel } from '../components/Dashboard/StatsPanel';
import { ApiException } from '../api/client';
import styles from './DashboardPage.module.css';

export const DashboardPage: React.FC = () => {
  const { urls, fetchUrls, createUrl, hasMore, isLoading } = useUrlStore();

  const [originalUrl, setOriginalUrl] = useState('');
  const [customSlug, setCustomSlug] = useState('');
  const [ttlDays, setTtlDays] = useState('');
  const [maxCount, setMaxCount] = useState('');

  const [isCreating, setIsCreating] = useState(false);
  const [errors, setErrors] = useState<string[]>([]);
  const [selectedUrlId, setSelectedUrlId] = useState<string | null>(null);

  useEffect(() => {
    fetchUrls(true).catch(() => { });
  }, [fetchUrls]);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors([]);

    if (!originalUrl) {
      setErrors(['Destination URL is required']);
      return;
    }

    setIsCreating(true);
    try {
      let expires_at = undefined;
      const days = ttlDays ? Math.min(parseInt(ttlDays, 10), 15) : 7;
      const date = new Date();
      date.setDate(date.getDate() + days);
      expires_at = date.toISOString();

      await createUrl({
        url: originalUrl,
        custom_slug: customSlug || undefined,
        expires_at,
        max_count: maxCount ? parseInt(maxCount, 10) : undefined
      });

      setOriginalUrl('');
      setCustomSlug('');
      setTtlDays('');
      setMaxCount('');
    } catch (err) {
      if (err instanceof ApiException) {
        setErrors(err.errors);
      } else {
        setErrors(['An unexpected error occurred while creating the link.']);
      }
    } finally {
      setIsCreating(false);
    }
  };

  return (
    <div className="container">
      <div className={styles.container}>
        <div className={styles.header}>
          <h1 className={styles.title}>My Links</h1>
        </div>

        <ErrorMessage errors={errors} onDismiss={() => setErrors([])} />

        <form className={styles.createForm} onSubmit={handleCreate}>
          <div className={styles.formGrid}>
            <Input
              label="Destination URL"
              placeholder="https://example.com/custom_path"
              value={originalUrl}
              onChange={(e) => setOriginalUrl(e.target.value)}
            />
            <div style={{ position: 'relative' }}>
              <Input
                label={
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    Custom Slug
                    <span
                      style={{ cursor: 'help', color: 'var(--color-gray-400)', display: 'inline-flex' }}
                      data-tooltip="Format: Only letters, numbers, hyphens, and underscores. Length: 5-30."
                    >
                      <Info size={16} />
                    </span>
                  </div>
                }
                placeholder="my-campaign"
                value={customSlug}
                onChange={(e) => setCustomSlug(e.target.value)}
                pattern="^[a-zA-Z0-9_-]{5,30}$"
                minLength={5}
                maxLength={30}
              />
            </div>
            <div style={{ position: 'relative' }}>
              <Input
                label={
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    TTL in Days
                    <span
                      style={{ cursor: 'help', color: 'var(--color-gray-400)', display: 'inline-flex' }}
                      data-tooltip="If not provided, TTL = 7 by default"
                    >
                      <Info size={16} />
                    </span>
                  </div>
                }
                type="number"
                placeholder="7"
                min="1"
                max="15"
                value={ttlDays}
                onChange={(e) => setTtlDays(e.target.value)}
              />
            </div>
            <Input
              label="Max Clicks"
              type="number"
              placeholder="100"
              min="1"
              value={maxCount}
              onChange={(e) => setMaxCount(e.target.value)}
            />
            <Button type="submit" isLoading={isCreating}>Shorten</Button>
          </div>
        </form>

        <div className={styles.list}>
          {urls.length === 0 ? (
            <div className={styles.emptyState}>
              <p>You haven't created any links yet.</p>
            </div>
          ) : (
            urls.map((url) => (
              <UrlCard
                key={url.id}
                url={url}
                onClick={() => setSelectedUrlId(url.id)}
              />
            ))
          )}
        </div>
        {hasMore && urls.length > 0 && (
          <div style={{ display: 'flex', justifyContent: 'center', marginTop: '2rem', marginBottom: '2rem' }}>
            <Button onClick={() => fetchUrls()} isLoading={isLoading}>
              Load More
            </Button>
          </div>
        )}
      </div>

      {selectedUrlId && (
        <StatsPanel
          urlId={selectedUrlId}
          onClose={() => setSelectedUrlId(null)}
        />
      )}
    </div>
  );
};
