/*
import React, { useEffect, useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Container, Row, Col, Image, Spinner, Alert, Form, Button } from "react-bootstrap";
import { FaEdit } from "react-icons/fa";
import { MdOutlineCameraAlt } from "react-icons/md";
import ArticleModal from "./ArticleModal";
import ArticleCard from "./ArticleCard";
import {
fetchUserProfile,
updateProfileImage,
fetchUserArticles,
updateArticle,
updateUserProfile,
updateAuthUserProfileImage,
        } from "../redux/actions/index";

        const ProfilePage = () => {
        const dispatch = useDispatch();
  const { userProfile: user, articles: userArticles, error } = useSelector((state) => state.profile);

        const [loading, setLoading] = useState(true);
  const [selectedArticle, setSelectedArticle] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [profileDescription, setProfileDescription] = useState("");
  const [isEditingProfileDescription, setIsEditingProfileDescription] = useState(false);
  const [isEditingArticle, setIsEditingArticle] = useState(false);

  const fileInputRef = useRef();

useEffect(() => {
        const token = localStorage.getItem("token");
    if (!token) {
dispatch({ type: "SET_PROFILE_ERROR", payload: "Utente non autenticato." });
setLoading(false);
      return;
              }

              const loadProfile = async () => {
        try {
        const userData = await fetchUserProfile(token);
dispatch({ type: "SET_USER_PROFILE", payload: userData });
setProfileDescription(userData.description || "");

        const articles = await fetchUserArticles(token);
dispatch({
    type: "SET_USER_ARTICLES",
            payload: articles.sort((a, b) => b.id - a.id),
});
        } catch (err) {
dispatch({ type: "SET_PROFILE_ERROR", payload: err.message });
        } finally {
setLoading(false);
      }
              };

loadProfile();
  }, [dispatch]);

        const handleImageChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        try {
        const updatedUser = await updateProfileImage(file);
dispatch({ type: "SET_USER_PROFILE", payload: updatedUser });
dispatch(updateAuthUserProfileImage(updatedUser));
        } catch (err) {
        console.error(err);
alert("Errore nel caricamento dell'immagine");
    }
            };

            const handleSaveProfileDescription = async () => {
        const token = localStorage.getItem("token");
    try {
            const updatedUser = await updateUserProfile(token, {
    description: profileDescription,
            profileImage: user.profileImage,
});
dispatch({ type: "SET_USER_PROFILE", payload: updatedUser });
setIsEditingProfileDescription(false);
    } catch (err) {
alert(err.message);
    }
            };

            const handleCardClick = (article) => {
setSelectedArticle(article);
setShowModal(true);
  };

          const handleSaveArticle = async (articleId, formData) => {
        const token = localStorage.getItem("token");
    try {
            const updatedArticle = await updateArticle(token, articleId, formData);
dispatch({
    type: "SET_USER_ARTICLES",
            payload: userArticles.map((a) => (a.id === updatedArticle.id ? updatedArticle : a)),
});
setIsEditingArticle(false);
setShowModal(false);
    } catch (err) {
alert(err.message);
    }
            };

            if (loading) {
        return (
      <Container className="text-center py-5">
        <Spinner animation="border" />
      </Container>
        );
        }

        if (error) {
        return (
      <Container className="text-center py-5">
        <Alert variant="danger">{error}</Alert>
      </Container>
        );
        }

        return (
    <Container className="py-5">
      <Row className="align-items-center mb-4">
        <Col md="auto" className="text-center position-relative">
          <div className="profile-avatar position-relative">
            <Image
src={user.profileImage || "https://cdn-icons-png.flaticon.com/512/847/847969.png"}
alt="Foto profilo"
roundedCircle
        className="profile-avatar-img"
width={130}
height={130}
        />
            <div
onClick={() => fileInputRef.current.click()}
className="custom-camera-wrapper position-absolute bottom-0 end-0"
title="Cambia foto profilo"
style={{ cursor: "pointer", backgroundColor: "#fff", borderRadius: "50%", padding: "4px" }}
        >
              <MdOutlineCameraAlt size={20} />
            </div>
            <Form.Control
        type="file"
accept="image/*"
onChange={handleImageChange}
ref={fileInputRef}
style={{ display: "none" }}
        />
          </div>
        </Col>

        <Col>
          <h3 className="fw-bold">{user.username}</h3>
          <p className="text-muted">{user.email}</p>
        {isEditingProfileDescription ? (
            <>
              <Form.Control
        as="textarea"
rows={2}
value={profileDescription}
onChange={(e) => setProfileDescription(e.target.value)}
        />
              <div className="text-end mt-2">
                <Button
size="sm"
variant="secondary"
className="me-2"
onClick={() => {
setProfileDescription(user.description || "");
setIsEditingProfileDescription(false);
                  }}
                          >
Annulla
        </Button>
                <Button size="sm" variant="success" onClick={handleSaveProfileDescription}>
Salva
        </Button>
              </div>
            </>
        ) : (
            <div className="d-flex align-items-center">
              <p className="mb-0 me-2 flex-grow-1 text-muted">
        {user.description?.trim() || "Nessuna descrizione disponibile."}
              </p>
              <FaEdit
size={18}
onClick={() => setIsEditingProfileDescription(true)}
style={{ cursor: "pointer" }}
title="Modifica descrizione"
        />
            </div>
        )}
        </Col>
      </Row>

      <hr className="my-5" />

<h4>I tuoi annunci</h4>
        {userArticles.length === 0 ? (
<p>Nessun articolo pubblicato.</p>
        ) : (
<Row>
{userArticles.map((article) => (
                <div key={article.id} className="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-2-4-custom mb-4">
                <ArticleCard
        article={article}
        onClick={handleCardClick}
        showFavoriteIcon={false} // 🔒 Disattiva icona "cuore" nella pagina profilo
                />
                </div>
))}
        </Row>
        )}

      <ArticleModal
show={showModal}
handleClose={() => {
setShowModal(false);
setIsEditingArticle(false);
        }}
article={selectedArticle}
setArticle={setSelectedArticle}
isEditing={isEditingArticle}
setIsEditing={setIsEditingArticle}
handleSave={handleSaveArticle}
        />
    </Container>
        );
        };

export default ProfilePage;
*/
