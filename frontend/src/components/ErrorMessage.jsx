export default function ErrorMessage({ error }) {
  if (!error) {
    return null;
  }

  return (
    <div className="error-card" role="alert">
      <div className="error-badge">Error</div>
      <h3>{error.title || 'Request failed'}</h3>
      <p>{error.message || 'Something went wrong while analyzing the URL.'}</p>
      {error.status ? <span className="error-meta">HTTP {error.status}</span> : null}
    </div>
  );
}