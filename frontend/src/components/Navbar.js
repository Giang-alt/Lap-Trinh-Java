import React from 'react';
import { Navbar as BootstrapNavbar, Nav, Container, NavDropdown } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <BootstrapNavbar bg="light" expand="lg" className="mb-4">
      <Container>
        <BootstrapNavbar.Brand as={Link} to="/">
          EV Data Analytics Marketplace
        </BootstrapNavbar.Brand>
        
        <BootstrapNavbar.Toggle aria-controls="basic-navbar-nav" />
        <BootstrapNavbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/">Trang chủ</Nav.Link>
            {/* Chỉ hiển thị link "Dữ liệu" cho Consumer và Admin, không hiển thị cho Provider */}
            {(!user || user.role === 'DATA_CONSUMER' || user.role === 'ADMIN') && (
              <Nav.Link as={Link} to="/data-packages">Dữ liệu</Nav.Link>
            )}
          </Nav>
          
          <Nav>
            {user ? (
              <NavDropdown title={`Xin chào, ${user.username}`} id="basic-nav-dropdown">
                {user.role === 'DATA_PROVIDER' && (
                  <NavDropdown.Item as={Link} to="/provider">
                    Dashboard Nhà cung cấp
                  </NavDropdown.Item>
                )}
                {user.role === 'ADMIN' && (
                  <NavDropdown.Item as={Link} to="/admin">
                    Dashboard Admin
                  </NavDropdown.Item>
                )}
                <NavDropdown.Divider />
                <NavDropdown.Item onClick={handleLogout}>
                  Đăng xuất
                </NavDropdown.Item>
              </NavDropdown>
            ) : (
              <>
                <Nav.Link as={Link} to="/login">Đăng nhập</Nav.Link>
                <Nav.Link as={Link} to="/register">Đăng ký</Nav.Link>
              </>
            )}
          </Nav>
        </BootstrapNavbar.Collapse>
      </Container>
    </BootstrapNavbar>
  );
};

export default Navbar;
