import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Register from './Register';
import { BrowserRouter as Router } from 'react-router-dom';

describe('Register', () => {
  test('renders without errors', () => {
    render(<Router>
      <Register />
      </Router>
      );
    // Assert that the component renders without throwing any errors
  });

  test('displays a registration form', () => {
    render(
      <Router>
        <Register />
      </Router>,
    );
    // Assert that the registration form elements are displayed on the screen
    expect(screen.getByLabelText('First Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Last Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
    expect(screen.getByLabelText('Password')).toBeInTheDocument();
    expect(screen.getByLabelText('Confirm Password')).toBeInTheDocument();
    expect(
      screen.getByRole('button', { name: 'Register' }),
    ).toBeInTheDocument();
  });

  test('submits the registration form with valid data', () => {
    render(
      <Router>
        <Register />
      </Router>,
    );
    // Fill in the registration form with valid data
    fireEvent.change(screen.getByLabelText('First Name'), {
      target: { value: 'Ramisetty' },
    });
    fireEvent.change(screen.getByLabelText('Last Name'), {
      target: { value: 'Parimala' },
    });
    fireEvent.change(screen.getByLabelText('Email'), {
      target: { value: 'ramisettyparimala@gmail.com' },
    });
    fireEvent.change(screen.getByLabelText('Password'), {
      target: { value: 'Password@34' },
    });
    fireEvent.change(screen.getByLabelText('Confirm Password'), {
      target: { value: 'Password@34' },
    });

    // Submit the form
    fireEvent.click(screen.getByRole('button', { name: 'Register' }));

    // Assert that the form submission is successful
    // You can check for success messages or navigate to a new page
  });

  test('displays an error message when form submission fails', () => {
    render(
      <Router>
        <Register />
      </Router>,
    );
    // Fill in the registration form with invalid data
    fireEvent.change(screen.getByLabelText('First Name'), {
      target: { value: '' },
    });
    fireEvent.change(screen.getByLabelText('Last Name'), {
      target: { value: '' },
    });
    fireEvent.change(screen.getByLabelText('Email'), {
      target: { value: '' },
    });
    fireEvent.change(screen.getByLabelText('Password'), {
      target: { value: '' },
    });
    fireEvent.change(screen.getByLabelText('Confirm Password'), {
      target: { value: '' },
    });

    // Submit the form
    fireEvent.click(screen.getByRole('button', { name: 'Register' }));

    // Assert that an error message is displayed on the screen
    // expect(screen.getByText('Email is required')).toBeInTheDocument();
    // expect(screen.getByText('Passwords do not match')).toBeInTheDocument();
  });
});
