import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { User } from '../../interfaces/user.interface';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    logOut: jest.fn(),
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(
      of({
        id: 1,
        email: 'test@test.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: true,
        password: 'testpassword',
        createdAt: new Date(),
        updatedAt: new Date(),
      } as User)
    ),
    delete: jest.fn().mockReturnValue(of(null)),
  };

  const mockRouter = {
    navigate: jest.fn(),
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should call userService.getById and set the user property', () => {
      component.ngOnInit();
      expect(mockUserService.getById).toHaveBeenCalledWith(
        mockSessionService.sessionInformation.id.toString()
      );
      expect(component.user).toEqual({
        id: 1,
        email: 'test@test.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: true,
        password: 'testpassword',
        createdAt: expect.any(Date),
        updatedAt: expect.any(Date),
      });
    });
  });

  describe('back', () => {
    it('should call window.history.back', () => {
      jest.spyOn(window.history, 'back');
      component.back();
      expect(window.history.back).toHaveBeenCalled();
    });
  });

  describe('delete', () => {
    it('should call userService.delete, show a snackbar and navigate to home', () => {
      component.delete();
      expect(mockUserService.delete).toHaveBeenCalledWith(
        mockSessionService.sessionInformation.id.toString()
      );
      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        { duration: 3000 }
      );
      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
    });
  });
});
