export enum ColumnId {
  BACKLOG_SR1 = 'backlog_sr1',
  BACKLOG_SR2 = 'backlog_sr2',
  DOING = 'doing',
  REVIEW = 'review',
  DONE = 'done'
}

export enum TaskCategory {
  PROCESSO = 'Processo',
  TECNICO = 'Técnico',
  KICKOFF = 'Kickoff',
  SR1 = 'SR1',
  SR2 = 'SR2'
}

export interface Task {
  id: string;
  title: string;
  description?: string;
  poFeedback?: string;
  userStories?: string[];
  dueDate: string;
  columnId: ColumnId;
  category: TaskCategory;
  priority: 'Baixa' | 'Média' | 'Alta';
}

export interface TeamMember {
  id: string;
  name: string;
  role: string;
  photo: string;
}

export interface Milestone {
  id: string;
  title: string;
  date: string;
  description: string;
  tasks: string[]; // Task IDs
}
