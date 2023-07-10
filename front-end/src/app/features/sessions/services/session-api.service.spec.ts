import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a list of sessions', () => {
    const mockSessions: Session[] = [
      {
        id: 1,
        name: 'Session 1',
        description: 'Test session 1',
        date: new Date(),
        teacher_id: 1,
        users: [2, 3],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        name: 'Session 2',
        description: 'Test session 2',
        date: new Date(),
        teacher_id: 2,
        users: [3, 4],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    service.all().subscribe((sessions) => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(mockSessions);
    });
    const req = httpMock.expectOne(`${service.getPathService()}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should return a session by id', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Test session 1',
      date: new Date(),
      teacher_id: 1,
      users: [2, 3],
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    service.detail('1').subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    const req = httpMock.expectOne(`${service.getPathService()}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session by id', () => {
    service.delete('1').subscribe();
    const req = httpMock.expectOne(`${service.getPathService()}/1`);
    expect(req.request.method).toBe('DELETE');
  });

  it('should create a new session', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Test session 1',
      date: new Date(),
      teacher_id: 1,
      users: [2, 3],
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    service.create(mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    const req = httpMock.expectOne(`${service.getPathService()}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockSession);
    req.flush(mockSession);
  });

  it('should update a session by id', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Test session 1',
      date: new Date(),
      teacher_id: 1,
      users: [2, 3],
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    service.update('1', mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    const req = httpMock.expectOne(`${service.getPathService()}/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockSession);
    req.flush(mockSession);
  });

 it('should allow a user to participate in a session', () => {
  const sessionId = '1';
  const userId = '2';
  service.participate(sessionId, userId).subscribe((response) => {
    expect(response).toBeFalsy();
  });
  const req = httpMock.expectOne(`${service.getPathService()}/${sessionId}/participate/${userId}`);
  expect(req.request.method).toBe('POST');
  expect(req.request.body).toBeNull();
  req.flush(null);
 });
  
  it('should allow a user to leave a session', () => {
    const sessionId = '1';
    const userId = '2';
    service.unParticipate(sessionId, userId).subscribe((response) => {
      expect(response).toBeFalsy();
    });
    const req = httpMock.expectOne(
      `${service.getPathService()}/${sessionId}/participate/${userId}`
    );
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

});