export type AuthResponse = {
  token: string;
};

export type AccountSummary = {
  id: string;
  ownerEmail: string;
  balance: number;
};

export type StatementEntry = {
  id: string;
  type: string;
  amount: number;
  description: string;
  createdAt: string;
};
