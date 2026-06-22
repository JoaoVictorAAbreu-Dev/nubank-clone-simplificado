import React from 'react';
import ReactDOM from 'react-dom/client';
import { Panel } from './components/Panel';
import { api } from './api/client';
import './styles.css';

type StatementItem = {
  id: string;
  accountEmail: string;
  type: string;
  amount: number;
  description: string;
  createdAt: string;
};

function App() {
  const [token, setToken] = React.useState('');
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [balance, setBalance] = React.useState('R$ 0,00');
  const [statement, setStatement] = React.useState<StatementItem[]>([]);
  const [toEmail, setToEmail] = React.useState('');
  const [amount, setAmount] = React.useState('100');
  const [description, setDescription] = React.useState('Movimentacao');
  const [message, setMessage] = React.useState('Faca login ou crie uma conta para carregar os dados.');
  const [loading, setLoading] = React.useState(false);

  function formatCurrency(value: number) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  function validateAuthForm() {
    if (!email.includes('@') || password.length < 8) {
      setMessage('Informe um email valido e senha com pelo menos 8 caracteres.');
      return false;
    }
    return true;
  }

  function validateMovementForm(requireDestination = false) {
    if (Number(amount) <= 0) {
      setMessage('Informe um valor maior que zero.');
      return false;
    }
    if (requireDestination && !toEmail.includes('@')) {
      setMessage('Informe um email de destino valido.');
      return false;
    }
    return true;
  }

  async function refreshAccount(currentToken = token) {
    const [dash, items] = await Promise.all([
      api.dashboard(currentToken),
      api.statements(currentToken),
    ]);
    setBalance(formatCurrency(dash.balance));
    setStatement(items);
  }

  async function authenticate(mode: 'login' | 'register') {
    if (!validateAuthForm()) return;
    setLoading(true);
    try {
      const result = mode === 'login'
        ? await api.login(email, password)
        : await api.register(email, password);
      setToken(result.token);
      await refreshAccount(result.token);
      setMessage(mode === 'login' ? 'Conta carregada com sucesso.' : 'Conta criada e carregada com sucesso.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Erro ao autenticar.');
    } finally {
      setLoading(false);
    }
  }

  async function handleLogin(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await authenticate('login');
  }

  async function handleDeposit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!validateMovementForm()) return;
    await api.deposit(token, Number(amount), description);
    await refreshAccount();
    setMessage('Deposito registrado.');
  }

  async function handleTransfer(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!validateMovementForm(true)) return;
    await api.transfer(token, toEmail, Number(amount), description);
    await refreshAccount();
    setMessage('Transferencia enviada.');
  }

  async function handlePix(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!validateMovementForm(true)) return;
    await api.pix(token, toEmail, Number(amount), description);
    await refreshAccount();
    setMessage('PIX ficticio concluido.');
  }

  return (
    <main className="app-shell">
      <section className="hero">
        <p className="eyebrow">TaskFlow Dev</p>
        <h1>Nubank Clone Simplificado</h1>
        <p className="subtitle">
          Login, saldo, transferencias, PIX ficticio e extrato em uma interface enxuta.
        </p>
      </section>

      <section className="panel">
        <Panel>
          <h2>Entrar</h2>
          <form className="stack" onSubmit={handleLogin}>
            <input value={email} onChange={(event) => setEmail(event.target.value)} placeholder="Email" />
            <input value={password} onChange={(event) => setPassword(event.target.value)} placeholder="Senha" type="password" />
            <button type="submit" disabled={loading}>{loading ? 'Carregando...' : 'Entrar'}</button>
            <button type="button" disabled={loading} onClick={() => authenticate('register')}>Criar conta</button>
          </form>
        </Panel>

        <Panel>
          <h2>Resumo da conta</h2>
          <p className="balance">{balance}</p>
          <p>Ultimos lancamentos e movimentacoes aparecem aqui.</p>
        </Panel>

        <Panel>
          <h2>Deposito</h2>
          <form className="stack" onSubmit={handleDeposit}>
            <input value={amount} onChange={(event) => setAmount(event.target.value)} placeholder="Valor" />
            <input value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Descricao" />
            <button type="submit" disabled={!token}>Depositar</button>
          </form>
        </Panel>

        <Panel>
          <h2>Transferir / PIX</h2>
          <form className="stack" onSubmit={handleTransfer}>
            <input value={toEmail} onChange={(event) => setToEmail(event.target.value)} placeholder="Destino" />
            <input value={amount} onChange={(event) => setAmount(event.target.value)} placeholder="Valor" />
            <input value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Descricao" />
            <button type="submit" disabled={!token}>Transferir</button>
          </form>
          <form className="stack" onSubmit={handlePix}>
            <button type="submit" disabled={!token}>Enviar PIX ficticio</button>
          </form>
        </Panel>

        <Panel>
          <h2>Extrato</h2>
          <div className="statement-list">
            {statement.length === 0 ? (
              <p>Sem lancamentos no momento.</p>
            ) : (
              statement.map((item) => (
                <article key={item.id} className="statement-item">
                  <strong>{item.type}</strong>
                  <span>{formatCurrency(item.amount)}</span>
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
