import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService],
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get a user by id', () => {
    const mockUser: User = {
      id: 1,
      email: 'testuser@example.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
      password: 'testpassword',
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    service.getById('1').subscribe((user) => {
      expect(user).toEqual(mockUser);
    });
    const req = httpMock.expectOne(`${service.getPathService()}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should delete a user by id', () => {
    service.delete('1').subscribe();
    const req = httpMock.expectOne(`${service.getPathService()}/1`);
    expect(req.request.method).toBe('DELETE');
  });
});