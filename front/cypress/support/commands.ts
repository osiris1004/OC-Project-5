// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************

const login = (email: string, password: string, simultate: boolean = false) => {
  cy.visit('/login');
  if (simultate) {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'Session 1',
          date: '2023-04-01T09:00:00Z',
          teacher_id: 1234,
          description: 'This is a test session.',
          users: [5678, 9012],
          createdAt: '2023-03-20T12:34:56.789Z',
          updatedAt: '2023-03-20T12:34:56.789Z',
        },
        {
          id: 2,
          name: 'Session 2',
          date: '2023-04-02T10:00:00Z',
          teacher_id: 5678,
          description: 'This is another test session.',
          users: [1234],
          createdAt: '2023-03-21T13:45:00.123Z',
          updatedAt: '2023-03-22T14:56:00.234Z',
        },
      ]
    ).as('session');
    cy.intercept('POST', '/api/auth/register', {
      body: {
        id: 1,
        email: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'yoga@studio.com',
        firstName: 'admin',
        lastName: 'ADMIN',
        admin: true,
        createdAt: '2021-05-05T12:00:00.000Z',
        updatedAt: '2021-05-05T12:00:00.000Z',
      },
    }).as('me');
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Session 1',
        date: '2023-04-01T09:00:00Z',
        teacher_id: 1234,
        description: 'This is a test session.',
        users: [5678, 9012],
        createdAt: '2023-03-20T12:34:56.789Z',
        updatedAt: '2023-03-20T12:34:56.789Z',
      }
    ).as('sessionOne');
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [
        {
          id: 1234,
          lastName: 'Tatcher',
          firstName: 'Maggy',
          createdAt: '2023-03-20T12:34:56.789Z',
          updatedAt: '2023-03-20T12:34:56.789Z',
        },
        {
          id: 1235,
          lastName: 'Abyss',
          firstName: 'Aby',
          createdAt: '2023-03-20T12:34:56.789Z',
          updatedAt: '2023-03-20T12:34:56.789Z',
        },
      ]
    ).as('teacher');
    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session 5',
        date: '2023-04-01T09:00:00Z',
        teacher_id: 1234,
        description: 'This is a test session.',
        users: [5678, 9012],
        createdAt: '2023-03-20T12:34:56.789Z',
        updatedAt: '2023-03-20T12:34:56.789Z',
      },
    });
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1234',
      },
      {
        id: 1,
        lastName: 'Tatcher',
        firstName: 'Maggy',
        createdAt: '2023-03-20T12:34:56.789Z',
        updatedAt: '2023-03-20T12:34:56.789Z',
      }
    ).as('teacherOne');
    cy.intercept('POST', '/api/session', {
      body: {
        id: 1,
        name: 'Session 5',
        date: '2023-04-01T09:00:00Z',
        teacher_id: 1234,
        description: 'This is a test session.',
        users: [5678, 9012],
        createdAt: '2023-03-20T12:34:56.789Z',
        updatedAt: '2023-03-20T12:34:56.789Z',
      },
    });
  }
  cy.get('input[formControlName=email]').type(email);
  cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`);
};


declare namespace Cypress {
  interface Chainable<Subject = any> {
    login(email: string, password: string): typeof login;
  }
}

// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
Cypress.Commands.add("login", login)
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
