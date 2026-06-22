import type { PropsWithChildren } from 'react';

export function Panel({ children }: PropsWithChildren) {
  return <section className="card">{children}</section>;
}
