import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should register a new user', () => {
    const mockRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password',
    };
    service.register(mockRequest).subscribe((response) => {
      expect(response).toBeFalsy();
    });
    const req = httpMock.expectOne(`${service.getPathService()}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(null);
  });

  it('should log in an existing user', () => {
    const mockRequest: LoginRequest = {
      email: 'test@example.com',
      password: 'password',
    };
    const mockSession: SessionInformation = {
      token: 'abc123',
      type: 'Bearer',
      id: 1,
      username: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };
    service.login(mockRequest).subscribe((response) => {
      expect(response).toEqual(mockSession);
    });
    const req = httpMock.expectOne(`${service.getPathService()}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockSession);
  });
});
