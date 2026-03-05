import express from 'express';
import { createServer as createViteServer } from 'vite';
import { WebSocketServer, WebSocket } from 'ws';
import http from 'http';
import { INITIAL_TASKS, TEAM_MEMBERS } from './src/constants';
import { Task, TeamMember } from './src/types';

async function startServer() {
  const app = express();
  const server = http.createServer(app);
  const wss = new WebSocketServer({ server });

  // Server-side state (Source of Truth)
  let tasks: Task[] = [...INITIAL_TASKS];
  let team: TeamMember[] = [...TEAM_MEMBERS];
  let documents: { [id: string]: string } = {};

  // WebSocket connection handling
  wss.on('connection', (ws: WebSocket) => {
    console.log('New client connected');

    // Initial sync
    ws.send(JSON.stringify({
      type: 'INIT',
      payload: { tasks, team, documents }
    }));

    ws.on('message', (data: string) => {
      try {
        const message = JSON.parse(data);
        console.log('Received message:', message.type);

        switch (message.type) {
          case 'UPDATE_TASKS':
            tasks = message.payload;
            broadcast(wss, { type: 'TASKS_UPDATED', payload: tasks }, ws);
            break;
          case 'UPDATE_TEAM':
            team = message.payload;
            broadcast(wss, { type: 'TEAM_UPDATED', payload: team }, ws);
            break;
          case 'UPDATE_DOCS':
            documents = message.payload;
            broadcast(wss, { type: 'DOCS_UPDATED', payload: documents }, ws);
            break;
        }
      } catch (err) {
        console.error('Error parsing message:', err);
      }
    });

    ws.on('close', () => {
      console.log('Client disconnected');
    });
  });

  function broadcast(wss: WebSocketServer, message: any, sender?: WebSocket) {
    const data = JSON.stringify(message);
    wss.clients.forEach((client) => {
      if (client.readyState === WebSocket.OPEN && client !== sender) {
        client.send(data);
      }
    });
  }

  // Vite middleware for development
  if (process.env.NODE_ENV !== 'production') {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: 'spa',
    });
    app.use(vite.middlewares);
  } else {
    app.use(express.static('dist'));
  }

  const PORT = 3000;
  server.listen(PORT, '0.0.0.0', () => {
    console.log(`Server running on http://localhost:${PORT}`);
  });
}

startServer();
