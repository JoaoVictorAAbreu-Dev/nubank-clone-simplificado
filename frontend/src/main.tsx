import React from 'react';
import ReactDOM from 'react-dom/client';
import { Panel } from './components/Panel';
import { api } from './api/client';
import './styles.css';

function App() {
  const [token, setToken] = React.useState('');
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [balance, setBalance] = React.useState('R$ 0,00');
  const [statement, setStatement] = React.useState<Array<{ id: string; accountEmail: string; type: string; amount: number; description: string; createdAt: string }>>([]);
  const [toEmail, setToEmail] = React.useState('');
  const [amount, setAmount] = React.useState('100');
  const [description, setDescription] = React.useState('Movimentação');
  const [message, setMessage] = React.useState('Faça login para carregar a conta.');

  async function handleLogin(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    try {
      const result = await api.login(email, password);
      setToken(result.token);
      const [dash, items] = await Promise.all([
        api.dashboard(result.token),
        api.statements(result.token),
      ]);
      setBalance(new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(dash.balance));
      setStatement(items);
      setMessage('Conta carregada com sucesso.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Erro ao autenticar');
    }
  }

  async function refreshAccount(currentToken = token) {
    const [dash, items] = await Promise.all([
      api.dashboard(currentToken),
      api.statements(currentToken),
    ]);
    setBalance(new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(dash.balance));
    setStatement(items);
  }

  async function handleDeposit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await api.deposit(token, Number(amount), description);
    await refreshAccount();
    setMessage('Depósito registrado.');
  }

  async function handleTransfer(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await api.transfer(token, toEmail, Number(amount), description);
    await refreshAccount();
    setMessage('Transferência enviada.');
  }

  async function handlePix(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await api.pix(token, toEmail, Number(amount), description);
    await refreshAccount();
    setMessage('PIX fictício concluído.');
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
          <p className="balance">{balance}</p>
          <p>Últimos lançamentos e movimentações aparecem aqui.</p>
        </Panel>

        <Panel>
          <h2>Depósito</h2>
          <form className="stack" onSubmit={handleDeposit}>
            <input value={amount} onChange={(event) => setAmount(event.target.value)} placeholder="Valor" />
            <input value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Descrição" />
            <button type="submit" disabled={!token}>Depositar</button>
          </form>
        </Panel>

        <Panel>
          <h2>Transferir / PIX</h2>
          <form className="stack" onSubmit={handleTransfer}>
            <input value={toEmail} onChange={(event) => setToEmail(event.target.value)} placeholder="Destino" />
            <input value={amount} onChange={(event) => setAmount(event.target.value)} placeholder="Valor" />
            <input value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Descrição" />
            <button type="submit" disabled={!token}>Transferir</button>
          </form>
          <form className="stack" onSubmit={handlePix}>
            <button type="submit" disabled={!token}>Enviar PIX fictício</button>
          </form>
        </Panel>

        <Panel>
          <h2>Extrato</h2>
          <div className="statement-list">
            {statement.length === 0 ? (
              <p>Sem lançamentos no momento.</p>
            ) : (
              statement.map((item) => (
                <article key={item.id} className="statement-item">
                  <strong>{item.type}</strong>
                  <span>{new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(item.amount)}</span>
                  <small>{item.description}</small>
                </article>
              ))
            )}
          </div>
        </Panel>
      </section>

      <section className="footer-message">
        <p className="message">{message}</p>
      </section>
    </main>
  );
}

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
