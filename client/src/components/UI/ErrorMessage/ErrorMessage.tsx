import React from 'react';
import { X } from 'lucide-react';
import styles from './ErrorMessage.module.css';

interface ErrorMessageProps {
  errors?: string[] | string | null;
  onDismiss?: () => void;
}

export const ErrorMessage: React.FC<ErrorMessageProps> = ({ errors, onDismiss }) => {
  if (!errors || (Array.isArray(errors) && errors.length === 0)) return null;

  const errorList = Array.isArray(errors) ? errors : [errors];

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        {errorList.length === 1 ? (
          <span>{errorList[0]}</span>
        ) : (
          <ul className={styles.list}>
            {errorList.map((err, i) => (
              <li key={i}>{err}</li>
            ))}
          </ul>
        )}
      </div>
      {onDismiss && (
        <button onClick={onDismiss} className={styles.closeButton} aria-label="Dismiss error">
          <X size={16} />
        </button>
      )}
    </div>
  );
};
