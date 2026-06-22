import React from 'react';
import ReactDOM from 'react-dom/client';
import { Panel } from './components/Panel';
import { api } from './api/client';
import './styles.css';

function App() {
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [message, setMessage] = React.useState('Use login e transferência como base do fluxo.');

  async function handleLogin(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    try {
      const result = await api.login(email, password);
      setMessage(`Token obtido: ${result.token.slice(0, 16)}...`);
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Erro ao autenticar');
    }
  }

  return (
    <main className="app-shell">
      <section className="hero">
        <p className="eyebrow">TaskFlow Dev</p>
        <h1>Nubank Clone Simplificado</h1>
        <p className="subtitle">
          Login, saldo, transferências, PIX fictício e extrato em uma interface enxuta.
        </p>
      </section>

      <section className="panel">
        <Panel>
          <h2>Entrar</h2>
          <form className="stack" onSubmit={handleLogin}>
            <input value={email} onChange={(event) => setEmail(event.target.value)} placeholder="Email" />
            <input value={password} onChange={(event) => setPassword(event.target.value)} placeholder="Senha" type="password" />
            <button type="submit">Entrar</button>
          </form>
        </Panel>

        <Panel>
          <h2>Resumo da conta</h2>
          <p>Saldo: R$ 0,00</p>
          <p>Últimos lançamentos e movimentações aparecem aqui.</p>
        </Panel>

        <Panel>
          <h2>Fluxo</h2>
          <p>Transferências, PIX fictício, extrato e visão de conta seguem o mesmo contrato da API.</p>
          <p className="message">{message}</p>
        </Panel>
      </section>
    </main>
  );
}

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
