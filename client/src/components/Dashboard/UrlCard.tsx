import React, { useState } from 'react';
import { Copy, Edit2, Trash2, Check, BarChart2, X, Info } from 'lucide-react';
import toast from 'react-hot-toast';
import { useUrlStore } from '../../store/useUrlStore';
import type { UrlItem } from '../../store/useUrlStore';
import { Input } from '../UI/Input/Input';
import { Button } from '../UI/Button/Button';
import { REDIRECTOR_BASE_URL, ApiException } from '../../api/client';
import styles from './UrlCard.module.css';

interface UrlCardProps {
  url: UrlItem;
  onClick: () => void;
}

export const UrlCard: React.FC<UrlCardProps> = ({ url, onClick }) => {
  const { updateUrl, deleteUrl } = useUrlStore();
  const [isEditing, setIsEditing] = useState(false);
  const [editSlug, setEditSlug] = useState(url.customSlug);
  const [editMaxCount, setEditMaxCount] = useState(url.maxCount ? url.maxCount.toString() : '');
  const [editActive, setEditActive] = useState<string>(url.active !== false ? 'true' : 'false');
  const [copied, setCopied] = useState(false);

  const shortLink = `${REDIRECTOR_BASE_URL}/${url.customSlug}`;

  const handleCopy = (e: React.MouseEvent) => {
    e.stopPropagation();
    navigator.clipboard.writeText(shortLink);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleDelete = async (e: React.MouseEvent) => {
    e.stopPropagation();
    if (confirm('Are you sure you want to delete this link?')) {
      await deleteUrl(url.id);
    }
  };

  const handleEditClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    setEditSlug(url.customSlug || '');
    const minMaxCount = url.totalClicks > 0 ? url.totalClicks + 1 : 1;
    let initialMaxCount = '';
    if (url.maxCount) {
      initialMaxCount = Math.max(url.maxCount, minMaxCount).toString();
    }
    setEditMaxCount(initialMaxCount);
    setEditActive(url.active !== false ? 'true' : 'false');
    setIsEditing(true);
  };

  const handleCancelEdit = () => {
    setEditSlug(url.customSlug || '');
    setEditMaxCount(url.maxCount ? url.maxCount.toString() : '');
    setEditActive(url.active !== false ? 'true' : 'false');
    setIsEditing(false);
  };

  const handleEditSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log("MANO HANDLEING SUBMIT...")
    const updateData: any = {};

    if (editSlug.trim() !== '') {
      updateData.new_slug = editSlug;
    }
    const newMaxCount = editMaxCount ? parseInt(editMaxCount, 10) : undefined;
    if (newMaxCount !== undefined) {
      updateData.max_click_count = newMaxCount;
    }
    updateData.active = editActive === 'true';

    try {
      await updateUrl(url.id, updateData);
      setIsEditing(false);
      toast.success('Link updated successfully');
    } catch (err) {
      if (err instanceof ApiException) {
        toast.error(err.errors.join(', '));
      } else {
        toast.error('Failed to update link');
      }
    }
  };

  if (isEditing) {
    const minMaxCount = url.totalClicks > 0 ? url.totalClicks + 1 : 1;
    return (
      <div className={styles.card}>
        <form className={styles.editForm} onSubmit={handleEditSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', width: '100%' }}>
          <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
            <div style={{ flex: 1, position: 'relative' }}>
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
                value={editSlug}
                onChange={(e) => setEditSlug(e.target.value)}
                placeholder="custom-slug"
                autoFocus
              />
            </div>

            <div style={{ flex: 1, position: 'relative' }}>
              <Input
                label={
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    Max Clicks
                    {url.totalClicks > 0 && (
                      <span
                        style={{ cursor: 'help', color: 'var(--color-gray-400)', display: 'inline-flex' }}
                        data-tooltip="As it already had some count, min max_click_count is attached to it"
                      >
                        <Info size={16} />
                      </span>
                    )}
                  </div>
                }
                type="number"
                min={minMaxCount}
                placeholder="Max Clicks"
                value={editMaxCount}
                onChange={(e) => setEditMaxCount(e.target.value)}
              />
            </div>

            <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 500, color: 'var(--color-gray-700)', marginBottom: '0.5rem' }}>
                Active Status
              </label>
              <button
                type="button"
                onClick={() => setEditActive(editActive === 'true' ? 'false' : 'true')}
                style={{
                  padding: '0.65rem 1rem',
                  borderRadius: '999px',
                  fontWeight: 'bold',
                  border: 'none',
                  cursor: 'pointer',
                  backgroundColor: editActive === 'true' ? 'var(--color-success)' : 'var(--color-error)',
                  color: 'white',
                  width: 'fit-content'
                }}
              >
                {editActive === 'true' ? 'TRUE' : 'FALSE'}
              </button>
            </div>
          </div>
          <div style={{ display: 'flex', gap: '0.5rem', justifyContent: 'flex-end' }}>
            <Button type="submit">Save</Button>
            <Button type="button" variant="secondary" onClick={handleCancelEdit}>
              <X size={16} />
            </Button>
          </div>
        </form>
      </div>
    );
  }

  return (
    <div className={styles.card} onClick={onClick}>
      <div className={styles.info}>
        <div className={styles.shortLinkWrapper}>
          <span className={styles.shortLink}>{shortLink}</span>
          <button
            className={styles.copyButton}
            onClick={handleCopy}
            data-tooltip="Copy to clipboard"
          >
            {copied ? <Check size={16} className="text-success" /> : <Copy size={16} />}
          </button>
          {copied && <span className="text-xs text-success">Copied!</span>}
        </div>
        <a
          href={url.originalUrl || (url as any).url || '#'}
          target="_blank"
          rel="noopener noreferrer"
          className={styles.originalUrl}
          onClick={(e) => e.stopPropagation()}
          title={url.originalUrl || (url as any).url || ''}
        >
          {((url.originalUrl || (url as any).url) || '').length > 30 ? ((url.originalUrl || (url as any).url) || '').substring(0, 30) + '...' : ((url.originalUrl || (url as any).url) || '')}
        </a>
        <div className={styles.meta}>
          <span>{new Date(url.createdAt).toLocaleDateString('en-GB')}</span>
          {url.ttlDays && <span>Expires in {url.ttlDays} days</span>}
          <span>Max Clicks: {url.maxCount ?? '-'}</span>
        </div>
      </div>

      <div className={styles.actions}>
        <div className={styles.statsBadge}>
          <BarChart2 size={16} />
          {url.totalClicks} clicks
        </div>
        <button
          className={styles.iconButton}
          onClick={handleEditClick}
          data-tooltip="Edit Link"
        >
          <Edit2 size={16} />
        </button>
        <button
          className={`${styles.iconButton} ${styles.delete}`}
          onClick={handleDelete}
          data-tooltip="Delete Link"
        >
          <Trash2 size={16} />
        </button>
      </div>
    </div>
  );
};
