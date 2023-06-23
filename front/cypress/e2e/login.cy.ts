describe('Login spec', () => {

  it('Register ok', () => {
        
    cy.visit('/register');
    cy.get('input[formControlName=firstName]').type('black');
    cy.get('input[formControlName=lastName]').type('cover');
    cy.get('input[formControlName=email]').type(`yoga${Date.now().toString()}@studio.com`);
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);

    cy.url().should('include', '/login');


    // cy.visit('/register');

    // cy.intercept(
    // {
    //     method: 'GET',
    //     url: '/api/session',
    // },
    // []
    // ).as('session');

    // cy.get('input[formControlName=firstName]').type('yoga');
    // cy.get('input[formControlName=lastName]').type('studio');
    // cy.get('input[formControlName=email]').type(
    //   `yoga${Date.now().toString()}@studio.com`
    // );
    // cy.get('input[formControlName=password]').type(
    // `${'test!1234'}{enter}{enter}`
    // );

    // cy.url().should('include', '/login');
});

  /* it('Login successfull', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("blackCover@OC.com")
    cy.get('input[formControlName=password]').type(`${"1234"}{enter}`)

    cy.intercept('POST', '/api/auth/login', {
      //! the respond object containing mocked response body
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

  
      //! ensure that the user is redirected or navigated to  '/sessions'
    cy.url().should('include', '/sessions')
  }) */
});


