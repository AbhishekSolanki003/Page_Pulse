import { useEffect, useMemo, useState } from 'react';
import AuditForm from '../components/AuditForm';
import AuditResult from '../components/AuditResult';
import ErrorMessage from '../components/ErrorMessage';
import Footer from '../components/Footer';
import Loader from '../components/Loader';
import { auditUrl } from '../services/auditApi';

const HISTORY_STORAGE_KEY = 'page-pulse-history';
const THEME_STORAGE_KEY = 'page-pulse-theme';

function toDisplayError(error) {
  if (!error) {
    return null;
  }

  if (error.response?.data) {
    return {
      title: error.response.data.error || 'Request failed',
      message: error.response.data.message || 'Unable to complete the audit.',
      status: error.response.data.status,
    };
  }

  return {
    title: 'Network error',
    message: error.message || 'Unable to reach the backend service.',
  };
}

export default function Home() {
  const [report, setReport] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [history, setHistory] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem(HISTORY_STORAGE_KEY) || '[]');
    } catch {
      return [];
    }
  });
  const [theme, setTheme] = useState(() => localStorage.getItem(THEME_STORAGE_KEY) || 'dark');

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem(THEME_STORAGE_KEY, theme);
  }, [theme]);

  useEffect(() => {
    localStorage.setItem(HISTORY_STORAGE_KEY, JSON.stringify(history.slice(0, 8)));
  }, [history]);

  const historyItems = useMemo(() => history.slice(0, 8), [history]);

  const handleAnalyze = async (url) => {
    setLoading(true);
    setError(null);

    try {
      const data = await auditUrl(url);
      setReport(data);
      setHistory((current) => [{ url, at: new Date().toISOString(), status: data.status }, ...current].slice(0, 8));
    } catch (err) {
      setReport(null);
      setError(toDisplayError(err));
    } finally {
      setLoading(false);
    }
  };

  const copyJson = async () => {
    if (!report) {
      return;
    }

    await navigator.clipboard.writeText(JSON.stringify(report, null, 2));
  };

  const downloadJson = () => {
    if (!report) {
      return;
    }

    const blob = new Blob([JSON.stringify(report, null, 2)], { type: 'application/json' });
    const anchor = document.createElement('a');
    anchor.href = URL.createObjectURL(blob);
    anchor.download = `page-pulse-report-${Date.now()}.json`;
    anchor.click();
    URL.revokeObjectURL(anchor.href);
  };

  return (
    <div className="app-shell">
      <div className="ambient ambient-one" />
      <div className="ambient ambient-two" />

      <main className="dashboard">
        <header className="hero-card">
          <div>
            <span className="eyebrow">Page Pulse</span>
            <h1>Website SEO and page intelligence in one audit.</h1>
            <p>
              Analyze any public website URL and receive a clean report with status, response time, title, metadata,
              heading counts, missing alt attributes, and approximate visible word count.
            </p>
          </div>
          <div className="hero-stat">
            <span>Deploy ready</span>
            <strong>Render + Vercel</strong>
          </div>
        </header>

        <section className="content-grid">
          <div className="left-column">
            <AuditForm onSubmit={handleAnalyze} loading={loading} />

            {loading ? <Loader /> : null}
            <ErrorMessage error={error} />
            <AuditResult
              report={report}
              onCopy={copyJson}
              onDownload={downloadJson}
              theme={theme}
              onToggleTheme={() => setTheme((current) => (current === 'dark' ? 'light' : 'dark'))}
            />
          </div>

          <aside className="history-panel">
            <div className="history-header">
              <span className="section-label">Audit History</span>
              <h3>Recent URLs</h3>
            </div>
            {historyItems.length === 0 ? (
              <p className="history-empty">Your recent audits will appear here.</p>
            ) : (
              <ul className="history-list">
                {historyItems.map((item) => (
                  <li key={`${item.url}-${item.at}`}>
                    <strong>{item.url}</strong>
                    <span>Status {item.status}</span>
                  </li>
                ))}
              </ul>
            )}
          </aside>
        </section>
      </main>

      <Footer />
    </div>
  );
}