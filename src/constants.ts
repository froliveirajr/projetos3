import { Task, ColumnId, TaskCategory, TeamMember } from './types';

export const INITIAL_TASKS: Task[] = [
  // Marco 1: 14/03
  {
    id: 'm1-1',
    title: 'Project Model Canvas (Kickoff) + Apresentação',
    description: 'Definição estratégica do projeto usando o framework PMC.',
    userStories: ['Como stakeholder, quero entender a proposta de valor do projeto.'],
    dueDate: '2026-03-14',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.KICKOFF,
    priority: 'Alta'
  },
  // ... (rest of tasks will be updated in App.tsx state if needed, but I'll keep them simple here)
  {
    id: 'm1-2',
    title: 'Cronograma Macro / Road Map',
    dueDate: '2026-03-14',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'm1-3',
    title: 'Análise de Concorrentes',
    dueDate: '2026-03-14',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.KICKOFF,
    priority: 'Baixa'
  },
  {
    id: 'm1-4',
    title: 'Personas',
    dueDate: '2026-03-14',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.KICKOFF,
    priority: 'Média'
  },
  {
    id: 'm1-5',
    title: 'Descrição da Problemática',
    dueDate: '2026-03-14',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.KICKOFF,
    priority: 'Média'
  },
  {
    id: 'm1-6',
    title: 'Criar Google Sites do Projeto',
    dueDate: '2026-03-14',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },
  {
    id: 'm1-7',
    title: 'Criar Readme inicial do Projeto',
    dueDate: '2026-03-14',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.TECNICO,
    priority: 'Média'
  },

  // Marco 2: 21/03
  {
    id: 'm2-1',
    title: 'Definir Fluxo de Versionamento (GitFlow)',
    dueDate: '2026-03-21',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'm2-2',
    title: 'User Story Mapping (INVEST)',
    dueDate: '2026-03-21',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Alta'
  },
  {
    id: 'm2-3',
    title: 'Detalhamento BDD',
    dueDate: '2026-03-21',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'm2-4',
    title: 'Protótipo de Baixa Fidelidade (Figma)',
    dueDate: '2026-03-21',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },

  // Marco 3: 30/03
  {
    id: 'm3-1',
    title: 'Implementação 2 HUs Prioritárias (Spring Boot)',
    dueDate: '2026-03-30',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'm3-2',
    title: 'Gravar Screencast da Aplicação',
    dueDate: '2026-03-30',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },

  // Marco 4: 11/04
  {
    id: 'm4-1',
    title: 'Detalhamento Pontos Fortes e Melhorias',
    dueDate: '2026-04-11',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },
  {
    id: 'm4-2',
    title: 'Elaborar Análise SWOT',
    dueDate: '2026-04-11',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },

  // Marco 5: 18/04
  {
    id: 'm5-1',
    title: 'Apresentação SR1',
    dueDate: '2026-04-18',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.SR1,
    priority: 'Alta'
  },
  {
    id: 'm5-2',
    title: 'Realizar Retrospectiva',
    dueDate: '2026-04-18',
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },

  // SR2 Tasks
  {
    id: 'sr2-1',
    title: 'Implementar restante das HUs (Mín. 5)',
    dueDate: '2026-06-19',
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'sr2-2',
    title: 'Criar Testes Automatizados',
    dueDate: '2026-06-19',
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'sr2-3',
    title: 'Uso do Bug Tracker do GitHub',
    dueDate: '2026-06-19',
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'sr2-4',
    title: 'Persistência de 3 Entidades de Domínio',
    dueDate: '2026-06-19',
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'sr2-5',
    title: 'Apresentação Final SR2',
    dueDate: '2026-06-19',
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.SR2,
    priority: 'Alta'
  }
];

export const TEAM_MEMBERS: TeamMember[] = [
  { id: '1', name: 'Membro 1', role: 'Scrum Master', photo: 'https://picsum.photos/seed/member1/200' },
  { id: '2', name: 'Membro 2', role: 'Product Owner', photo: 'https://picsum.photos/seed/member2/200' },
  { id: '3', name: 'Membro 3', role: 'Dev Backend', photo: 'https://picsum.photos/seed/member3/200' },
  { id: '4', name: 'Membro 4', role: 'Dev Frontend', photo: 'https://picsum.photos/seed/member4/200' },
  { id: '5', name: 'Membro 5', role: 'QA / Tester', photo: 'https://picsum.photos/seed/member5/200' },
];

export const COLUMNS = [
  { id: ColumnId.BACKLOG_SR1, title: 'Backlog SR1' },
  { id: ColumnId.BACKLOG_SR2, title: 'Backlog SR2' },
  { id: ColumnId.DOING, title: 'Fazendo' },
  { id: ColumnId.REVIEW, title: 'Revisão/Testes' },
  { id: ColumnId.DONE, title: 'Concluído' }
];
