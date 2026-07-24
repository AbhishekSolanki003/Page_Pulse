export default function Loader() {
  return (
    <div className="loader-shell" role="status" aria-live="polite" aria-label="Analyzing page">
      <span className="loader-ring" />
      <div>
        <p className="loader-title">Analyzing page</p>
        <p className="loader-copy">Fetching HTML, parsing metadata, and measuring page signals.</p>
      </div>
    </div>
  );
}