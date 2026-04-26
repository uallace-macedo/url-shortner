import React, { useEffect, useState } from 'react';
import { X } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { apiClient } from '../../api/client';
import type { UrlStats } from '../../store/useUrlStore';
import { ErrorMessage } from '../UI/ErrorMessage/ErrorMessage';
import styles from './StatsPanel.module.css';

interface StatsPanelProps {
  urlId: string;
  onClose: () => void;
}

export const StatsPanel: React.FC<StatsPanelProps> = ({ urlId, onClose }) => {
  const [stats, setStats] = useState<UrlStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const data = await apiClient.get<UrlStats>(`/urls/${urlId}/stats`);
        setStats(data);
      } catch (err: any) {
        setError(err.message || 'Failed to load statistics');
      } finally {
        setIsLoading(false);
      }
    };

    if (urlId) {
      fetchStats();
    }
  }, [urlId]);

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.panel} onClick={(e) => e.stopPropagation()}>
        <div className={styles.header}>
          <h2 className={styles.title}>Link Statistics</h2>
          <button className={styles.closeButton} onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className={styles.content}>
          {error && <ErrorMessage errors={error} />}
          
          {isLoading ? (
            <div className={styles.loading}>Loading statistics...</div>
          ) : stats ? (
            <>
              <div className={styles.section}>
                <h3 className={styles.sectionTitle}>Total Clicks</h3>
                <div className={styles.totalClicks}>{stats.totalClicks}</div>
              </div>

              <div className={styles.section}>
                <h3 className={styles.sectionTitle}>Clicks (Last 7 Days)</h3>
                <div className={styles.chartContainer}>
                  <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={stats.clicksPerDay} margin={{ top: 10, right: 0, left: -20, bottom: 0 }}>
                      <defs>
                        <linearGradient id="colorCount" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="var(--color-accent)" stopOpacity={0.3}/>
                          <stop offset="95%" stopColor="var(--color-accent)" stopOpacity={0}/>
                        </linearGradient>
                      </defs>
                      <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="var(--color-gray-200)" />
                      <XAxis 
                        dataKey="date" 
                        axisLine={false} 
                        tickLine={false} 
                        tick={{ fontSize: 12, fill: 'var(--color-gray-500)' }}
                        tickFormatter={(value) => new Date(value).toLocaleDateString(undefined, { weekday: 'short' })}
                      />
                      <YAxis 
                        axisLine={false} 
                        tickLine={false} 
                        tick={{ fontSize: 12, fill: 'var(--color-gray-500)' }}
                        allowDecimals={false}
                      />
                      <Tooltip 
                        contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: 'var(--shadow-md)' }}
                        labelFormatter={(label) => new Date(label).toLocaleDateString()}
                      />
                      <Area 
                        type="monotone" 
                        dataKey="count" 
                        stroke="var(--color-accent)" 
                        strokeWidth={2}
                        fillOpacity={1} 
                        fill="url(#colorCount)" 
                      />
                    </AreaChart>
                  </ResponsiveContainer>
                </div>
              </div>

              <div className={styles.section}>
                <h3 className={styles.sectionTitle}>Top Referrers</h3>
                {stats.topReferrers.length === 0 ? (
                  <p className="text-gray-500 text-sm">No referrer data available.</p>
                ) : (
                  <div className={styles.referrersList}>
                    {stats.topReferrers.map((ref, index) => (
                      <div key={index} className={styles.referrerItem}>
                        <span className={styles.referrerName}>{ref.referrer || 'Direct / Unknown'}</span>
                        <span className={styles.referrerCount}>{ref.count}</span>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </>
          ) : null}
        </div>
      </div>
    </div>
  );
};
