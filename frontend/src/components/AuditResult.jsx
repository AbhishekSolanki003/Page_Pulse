import { useMemo } from 'react';

function formatJson(report) {
  return JSON.stringify(report, null, 2);
}

export default function AuditResult({ report, onCopy, onDownload, theme, onToggleTheme }) {
  const jsonText = useMemo(() => (report ? formatJson(report) : ''), [report]);

  if (!report) {
    return null;
  }

  return (
    <section className="result-panel">
      <div className="panel-topline">
        <div>
          <span className="section-label">Audit Report</span>
          <h2>{report.title || 'Untitled page'}</h2>
          <p className="result-subtitle">Status {report.status} · Response time {report.responseTime} ms</p>
        </div>
        <div className="result-actions">
          <button type="button" className="ghost-button" onClick={onToggleTheme}>
            {theme === 'dark' ? 'Light mode' : 'Dark mode'}
          </button>
          <button type="button" className="ghost-button" onClick={onCopy}>
            Copy JSON
          </button>
          <button type="button" className="ghost-button" onClick={onDownload}>
            Download JSON
          </button>
        </div>
      </div>

      <div className="metric-grid">
        <MetricCard label="Status" value={report.status} tone="blue" />
        <MetricCard label="Response Time" value={`${report.responseTime} ms`} tone="teal" />
        <MetricCard label="Title" value={report.title || 'Missing title'} tone="gold" />
        <MetricCard label="Meta Description" value={report.metaDescription || 'Missing meta description'} tone="violet" />
        <MetricCard label="H1 Count" value={report.h1Count} tone="cyan" />
        <MetricCard label="Missing Alt Images" value={report.missingAltImages} tone="rose" />
        <MetricCard label="Word Count" value={report.wordCount} tone="green" />
      </div>

      <div className="json-preview">
        <div className="json-header">
          <span>Raw JSON</span>
          <span className="json-size">{jsonText.length} chars</span>
        </div>
        <pre>{jsonText}</pre>
      </div>
    </section>
  );
}

function MetricCard({ label, value, tone }) {
  return (
    <article className={`metric-card tone-${tone}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}