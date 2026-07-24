import { useState } from 'react';

function isValidUrl(value) {
  try {
    const parsed = new URL(value);
    return ['http:', 'https:'].includes(parsed.protocol) && parsed.hostname !== 'localhost';
  } catch {
    return false;
  }
}

export default function AuditForm({ onSubmit, loading, initialValue = '' }) {
  const [url, setUrl] = useState(initialValue);
  const [localError, setLocalError] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const trimmed = url.trim();

    if (!isValidUrl(trimmed)) {
      setLocalError('Enter a valid http or https URL.');
      return;
    }

    setLocalError('');
    onSubmit(trimmed);
  };

  return (
    <form className="audit-form" onSubmit={handleSubmit}>
      <label htmlFor="audit-url">Website URL</label>
      <div className="input-row">
        <input
          id="audit-url"
          type="url"
          placeholder="https://openai.com"
          value={url}
          onChange={(event) => setUrl(event.target.value)}
          disabled={loading}
          onKeyDown={(event) => {
            if (event.key === 'Enter') {
              handleSubmit(event);
            }
          }}
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Analyzing...' : 'Analyze'}
        </button>
      </div>
      {localError ? <p className="form-error">{localError}</p> : <p className="form-help">Accepts public http and https URLs only.</p>}
    </form>
  );
}