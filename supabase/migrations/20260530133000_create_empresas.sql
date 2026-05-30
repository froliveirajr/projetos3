create table if not exists public.empresas (
  cnpj varchar(18) primary key,
  nome varchar(255) not null,
  senha varchar(255) not null,
  colaboradores integer not null check (colaboradores > 0),
  numero_beneficios integer not null check (numero_beneficios > 0),
  multibeneficio boolean not null,
  vida_util_cartao_anos double precision not null check (vida_util_cartao_anos > 0),
  taxa_turnover double precision not null check (taxa_turnover >= 0),
  taxa_reemissao double precision not null check (taxa_reemissao >= 0),
  transacoes_mensais integer not null check (transacoes_mensais >= 0),
  porcentagem_digital_atual double precision not null check (
    porcentagem_digital_atual >= 0
    and porcentagem_digital_atual <= 100
  )
);

create index if not exists idx_empresas_nome on public.empresas (nome);
