import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';
import { Input } from '../components/UI/Input/Input';
import { Button } from '../components/UI/Button/Button';
import { ErrorMessage } from '../components/UI/ErrorMessage/ErrorMessage';
import { ApiException } from '../api/client';
import styles from './AuthPage.module.css';

export const RegisterPage: React.FC = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  
  const { register } = useAuthStore();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors([]);
    
    if (!name || !email || !password) {
      setErrors(['Please fill in all fields']);
      return;
    }

    if (password.length < 6) {
      setErrors(['Password must be at least 6 characters']);
      return;
    }

    setIsLoading(true);
    try {
      await register({ name, email, password });
      navigate('/dashboard');
    } catch (err) {
      if (err instanceof ApiException) {
        setErrors(err.errors);
      } else {
        setErrors(['An unexpected error occurred.']);
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.wrapper}>
      <div className={styles.container}>
        <h2 className={styles.title}>Create an account</h2>
        
        <ErrorMessage errors={errors} onDismiss={() => setErrors([])} />
        
        <form onSubmit={handleSubmit} className={styles.form}>
          <Input 
            label="Name" 
            type="text" 
            value={name} 
            onChange={(e) => setName(e.target.value)} 
            placeholder="John Doe"
          />
          <Input 
            label="Email" 
            type="email" 
            value={email} 
            onChange={(e) => setEmail(e.target.value)} 
            placeholder="name@example.com"
          />
          <Input 
            label="Password" 
            type="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            placeholder="••••••••"
          />
          <Button type="submit" isLoading={isLoading} className="w-full mt-4">
            Sign up
          </Button>
        </form>
        
        <p className={styles.footer}>
          Already have an account? <Link to="/login" className={styles.link}>Sign in</Link>
        </p>
      </div>
    </div>
  );
};
