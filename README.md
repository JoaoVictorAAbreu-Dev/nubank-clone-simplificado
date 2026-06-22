# Nubank Clone Simplificado

Aplicação fullstack de demonstração financeira inspirada em fluxos bancários modernos. O objetivo é evidenciar regras de negócio, controle transacional, autenticação com JWT, persistência em PostgreSQL, uso pragmático de Redis e uma interface leve em React.

## Visão Geral

O projeto simula um ambiente bancário simplificado com:

- cadastro e autenticação de usuários;
- conta com controle de saldo;
- transferências internas;
- PIX fictício;
- extrato de movimentações;
- dashboard com resumo da conta;
- cache de leitura para consultas frequentes.

O sistema não integra serviços bancários reais. Todo o domínio é fictício e seguro para fins de portfólio.

## Stack

- Java 21
- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- Redis
- React
- TypeScript
- Vite
- Docker
- Docker Compose
- Swagger/OpenAPI
- JUnit
- Mockito

## Funcionalidades

- Registro e login com JWT
- Proteção de endpoints autenticados
- Controle de conta e saldo
- Depósito, transferência e PIX fictício
- Extrato com persistência das movimentações
- Cache de resumo de conta com Redis
- Documentação da API com Swagger

## Arquitetura

### Backend

O backend segue arquitetura em camadas:

- `auth/` autenticação e emissão de token
- `account/` conta, saldo e operações financeiras
- `statement/` extrato
- `dashboard/` leitura consolidada
- `report/` exportação de relatório
- `cache/` leitura otimizada com Redis
- `security/` filtro e validação de JWT
- `config/` segurança, OpenAPI e propriedades
- `exception/` tratamento centralizado de erros

### Frontend

A interface web foi construída em React com TypeScript e Vite, com foco em navegação enxuta e fluxo bancário básico:

- login;
- dashboard;
- transferências;
- PIX;
- extrato;
- resumo de conta.

## Estrutura do Projeto

- `backend/` API Spring Boot
- `frontend/` interface React
- `docker-compose.yml` serviços locais de apoio
- `.env.example` variáveis de ambiente esperadas

## Como Executar Localmente

1. Copie `.env.example` para `.env`.
2. Suba a infraestrutura:
   ```powershell
   docker compose up -d
   ```
3. Inicie o backend:
   ```powershell
   .\mvnw.cmd -f backend\pom.xml spring-boot:run
   ```
4. Em outro terminal, inicie o frontend:
   ```powershell
   cd frontend
   npm install
   npm run dev
   ```

## Documentação da API

Após subir o backend, a documentação fica disponível em:

- `http://localhost:8080/swagger-ui/index.html`

## Testes

Executar backend:

```powershell
.\mvnw.cmd -f backend\pom.xml test
```

Executar build do frontend:

```powershell
cd frontend
npm run build
```

## Decisões de Projeto

- PostgreSQL foi mantido como fonte de verdade.
- Redis é usado apenas para acelerar leituras de resumo.
- O frontend foi mantido simples para destacar o fluxo de produto, e não uma camada visual complexa.
- As regras de negócio vivem em services, evitando lógica espalhada em controllers.

## Melhorias Futuras

- expandir o extrato com filtros e paginação;
- adicionar notificações de limite;
- criar dashboard com gráficos mais ricos;
- incorporar testes end-to-end para os principais fluxos.
