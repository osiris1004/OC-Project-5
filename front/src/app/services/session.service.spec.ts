import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return an observable for isLogged', () => {
    const isLogged = service.$isLogged();
    expect(isLogged).toBeTruthy();
  });

  it('should log in a user', () => {
    const user: SessionInformation = {
      token: 'testtoken',
      type: 'testtype',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: true,
    };
    service.logIn(user);
    expect(service.sessionInformation).toEqual(user);
    expect(service.isLogged).toBeTruthy();
  });

  it('should log out a user', () => {
    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBeFalsy();
  });
});
